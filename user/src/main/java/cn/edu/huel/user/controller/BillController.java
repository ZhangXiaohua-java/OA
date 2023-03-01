package cn.edu.huel.user.controller;

import cn.edu.huel.base.StringUtils;
import cn.edu.huel.security.vo.Result;
import cn.edu.huel.user.base.constant.RedisConstant;
import cn.edu.huel.user.domain.PostOrder;
import cn.edu.huel.user.service.IPostOrderService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 * @author 张晓华
 * @date 2023-2-28
 */
@RestController
@RequestMapping("/bill")
public class BillController {

	@Resource
	private IPostOrderService postOrderService;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;


	@GetMapping("/list/orders")
	public Result queryBillToPay(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
								 @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
		List<PostOrder> orders = postOrderService.queryAllUnpaiedOrder(pageNum, pageSize);
		BigDecimal needPay = null;
		if (Objects.isNull(orders) || orders.isEmpty()) {
			needPay = new BigDecimal(0);
		} else {
			BigDecimal initialVal = new BigDecimal(0);
			needPay = orders.stream()
					.map(e -> e.getPay())
					.reduce(initialVal, (pre, next) -> {
						return pre.add(next);
					});
		}
		return Result.ok().put("data", needPay);
	}


	/**
	 * 支付 单个订单的支付请求
	 *
	 * @param orderId 订单号
	 */
	@PostMapping("/{orderId}")
	public Result payBill(@PathVariable String orderId) {
		// TODO 支付防重复提交,生成token,支付时先验证token
		String token = StringUtils.randomUUID();
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// 存放token,3分钟后过期
		ops.set(RedisConstant.PAY_TOKEN + id, token, Duration.ofMinutes(3));
		BigDecimal pay = postOrderService.queryBill(orderId);
		// 存放支付信息,3分钟后过期
		ops.set(RedisConstant.ORDER_PAY_INFO_PREFIX + orderId, pay, Duration.ofMinutes(3));
		String url = "http://localhost:8000/ali/trade/pay?orderSn=" + orderId + "&pay=" + pay.toPlainString()
				+ "&token=" + token + "&id=" + id;
		return Result.ok().put("data", url);
	}


}
