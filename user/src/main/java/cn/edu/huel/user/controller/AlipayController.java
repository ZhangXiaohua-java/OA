package cn.edu.huel.user.controller;

import cn.edu.huel.base.HashObjectMapper;
import cn.edu.huel.user.base.constant.OrderStatusEnum;
import cn.edu.huel.user.base.constant.RedisConstant;
import cn.edu.huel.user.base.ex.UnTrustedMessageException;
import cn.edu.huel.user.dto.AliMessage;
import cn.edu.huel.user.dto.AliPayNotifyMessage;
import cn.edu.huel.user.dto.MessageDto;
import cn.edu.huel.user.service.IPostOrderService;
import cn.edu.huel.user.to.TradeTo;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 支付宝支付相关
 *
 * @author 张晓华
 * @date 2022-12-14
 */
@Slf4j
@RestController
@RequestMapping("/ali")
public class AlipayController {

	@Resource
	private AlipayConfig alipayConfig;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Resource
	private IPostOrderService orderService;


	@GetMapping("/trade/pay")
	public String toPay(String orderSn, String pay, String token, String id) throws AlipayApiException {
		// TODO 防重复提交以及参数校验
		// 踩了个大坑,由于这里没有让Spring Security进行鉴权,导致获取不到用户的信息,因此导致Redis执行脚本一直返回false
		//String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ClassPathResource resource = new ClassPathResource("/script/VerifyToken.lua");
		RedisScript<Boolean> script = RedisScript.of(resource, Boolean.class);
		Boolean result = redisTemplate.execute(script, Collections.singletonList(RedisConstant.PAY_TOKEN + id), token);
		if (!result.booleanValue()) {
			return "请尝试重新支付";
		}
		// 验证支付金额是否有误,这里不要求原子性,所以就不再使用Lua脚本了
		BigDecimal storageMoney = (BigDecimal) redisTemplate.opsForValue().get(RedisConstant.ORDER_PAY_INFO_PREFIX + orderSn);
		if (!storageMoney.toPlainString().equals(pay)) {
			return "支付金额有误";
		}
		AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
		AlipayTradePagePayRequest pagePayRequest = new AlipayTradePagePayRequest();
		pagePayRequest.setNotifyUrl("https://65i3b70781.zicp.fun/ali/notify");
		pagePayRequest.setReturnUrl("https://65i3b70781.zicp.fun/success.html");
		System.out.println("orderSn" + orderSn);
		TradeTo tradeTo = new TradeTo(orderSn + "", "FAST_INSTANT_TRADE_PAY", pay, "post_fee", "zxh", "3m");
		tradeTo.setOut_trade_no(new String(tradeTo.getOut_trade_no().toString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
		tradeTo.setTotal_amount(new String(tradeTo.getTotal_amount().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
		tradeTo.setBody(new String(tradeTo.getBody().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
		tradeTo.setSubject(new String(tradeTo.getSubject().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
		tradeTo.setProduct_code(new String(tradeTo.getProduct_code().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
		tradeTo.setTimeout_express(new String(tradeTo.getTimeout_express().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
		pagePayRequest.setBizContent(JSON.toJSONString(tradeTo));
		return alipayClient.pageExecute(pagePayRequest).getBody();
	}

	// TODO 验单待做
	@PostMapping("/notify")
	// 卧槽,有一个大坑,由于升级到了Spring Boot3,Servlet API的包名也发生了变化,
	// 由于request对象使用的是之前版本的,导致注入不进来,就始终接收不到支付宝的通知信息
	//  Resolved [java.lang.IllegalStateException: No primary or single unique constructor found for interface javax.servlet.http.HttpServletRequest]
	public String callback(HttpServletRequest request) throws InvocationTargetException, InstantiationException, IllegalAccessException, AlipayApiException {
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			String value = request.getHeader(name);
			log.info("请求头的key: {}, value: {}", name, value);
		}
		HashMap<Object, Object> notifyMessage = new HashMap<>();
		HashMap<String, String> check = new HashMap<>();
		Map<String, String[]> map = request.getParameterMap();
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			String[] vals = map.get(key);
			String valStr = "";
			for (int i = 0; i < vals.length; i++) {
				// 处理有多个值的情况,有多个值用逗号分割
				valStr = (i == vals.length - 1) ? valStr + vals[i] : valStr + vals[i] + ",";
			}
			notifyMessage.put(key, valStr);
			check.put(key, valStr);
		}
		log.info("接收到支付宝的消息{}", JSON.toJSONString(notifyMessage));
		AliPayNotifyMessage message = new HashObjectMapper<AliPayNotifyMessage>().mappingToObject(notifyMessage, AliPayNotifyMessage.class);
		//	TODO 还需要验证订单号,支付金额等信息
		if (!message.getAuth_app_id().equals(alipayConfig.getAppId()) || !AlipaySignature.rsaCheckV1(check, alipayConfig.getAlipayPublicKey(), alipayConfig.getCharset(), alipayConfig.getSignType())) {
			throw new UnTrustedMessageException(check);
		}
		// TODO 验证通过,可以确定这就是来自支付宝的消息
		AliMessage aliMessage = JSON.parseObject(JSON.toJSONString(notifyMessage), AliMessage.class);
		if (aliMessage.getTrade_status().equals("TRADE_SUCCESS")) {
			log.info("接收到支付成功的消息");
			// 当前系统生成的订单号
			String tradeNo = aliMessage.getOut_trade_no();
			// 支付金额
			String amount = aliMessage.getTotal_amount();
			ValueOperations<String, Object> ops = redisTemplate.opsForValue();
			BigDecimal paied = (BigDecimal) ops.get(RedisConstant.ORDER_PAY_INFO_PREFIX + tradeNo);
			// TODO 更新订单状态为已支付
			orderService.updateOrderStatus(tradeNo, OrderStatusEnum.PAYED);
		}
		log.info("转换后的对象信息{}", message);
		return "success";
	}


	// 1677570073629
	@GetMapping("/query")
	public Object queryOrder(String orderSn) throws AlipayApiException {
		AlipayClient client = new DefaultAlipayClient(alipayConfig);
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizContent("{\"out_trade_no\": " + orderSn + " }");
		AlipayTradeQueryResponse response = client.execute(request);
		MessageDto dto = JSON.parseObject(JSON.toJSONString(response), MessageDto.class);
		return dto;
	}


}
