package cn.edu.huel.user.controller;


import cn.edu.huel.base.JwtUtils;
import cn.edu.huel.ex.JwtValidException;
import cn.edu.huel.security.config.JWTConfig;
import cn.edu.huel.security.domain.SecurityUser;
import cn.edu.huel.security.vo.Result;
import cn.edu.huel.user.base.constant.OrderStatusEnum;
import cn.edu.huel.user.base.constant.RedisConstant;
import cn.edu.huel.user.base.standard.CreateOrder;
import cn.edu.huel.user.base.utils.PostCostCounter;
import cn.edu.huel.user.domain.Customer;
import cn.edu.huel.user.domain.PostOrder;
import cn.edu.huel.user.message.MessageService;
import cn.edu.huel.user.service.IAreaService;
import cn.edu.huel.user.service.ICustomerService;
import cn.edu.huel.user.service.IPostOrderService;
import cn.edu.huel.user.to.OrderTo;
import cn.edu.huel.user.to.PostOrderTo;
import cn.edu.huel.user.to.TraceTo;
import cn.edu.huel.user.vo.*;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.utils.bean.BeanUtils;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 订单Controller
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@RestController
@RequestMapping("/order")
public class PostOrderController extends BaseController {


	@Resource
	private MessageService messageService;

	@Resource
	private IPostOrderService orderService;

	@Resource
	private IAreaService areaService;

	@Resource
	private PostCostCounter postCostCounter;

	@Resource
	private ICustomerService customerService;

	@Resource
	private JWTConfig config;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;


	/**
	 * @return 生成token令牌, 防止订单的重复提交
	 */
	@GetMapping("/token")
	public Result getToken() {
		String token = IdUtil.nanoId();
		String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		// token有效期三分钟
		ops.set(RedisConstant.ORDER_TOKEN + id, token, Duration.ofMinutes(3));
		return Result.ok().put("data", token);
	}


	/**
	 * 直接将校验过的数据全部投递到rabbitMQ异步消费即可
	 *
	 * @param orderVO 客户端提交过来的请求参数
	 * @return result
	 */
	@PostMapping("/create")
	public Result order(@RequestBody @Validated({CreateOrder.class}) OrderVO orderVO, @RequestHeader("token") String token) throws JwtValidException {
		UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		SecurityUser user = JwtUtils.decode(token, config.getSignKey(), SecurityUser.class);
		// 前端填充的信息,由于前端存储的手机号信息是脱敏的,所以需要重新设置
		if (orderVO.getPosterPhone().contains("*")) {
			orderVO.setPosterPhone(customerService.getById(authentication.getPrincipal().toString()).getPhone());
		}
		// 邮编和城市区号的兼容性处理
		orderVO.setPosterZipCode(areaService.codeExists(orderVO.getPosterZipCode()));
		String id = (String) authentication.getPrincipal();
		orderVO.setCustomerId(id);
		messageService.sendOrder(orderVO);
		return Result.ok();
	}


	/**
	 * 查询所有满足条件的订单
	 *
	 * @param condition 查询条件
	 * @return 订单信息
	 */
	@PostMapping("/query/condition")
	public Result orderDetail(@RequestBody Condition condition) {
		Page<PostOrder> page = orderService.queryOrdersByCondition(condition);
		List<OrderInfoVo> list = page.getRecords().stream().map(e -> {
			OrderInfoVo infoVo = new OrderInfoVo();
			BeanUtils.copyProperties(e, infoVo);
			String[] split = infoVo.getDest().split(",");
			String mergerName = areaService.getMergerNameByZipCode(split[0]);
			infoVo.setDest(mergerName + " " + split[1]);
			split = infoVo.getOrigin().split(",");
			mergerName = areaService.getMergerNameByZipCode(split[0]);
			infoVo.setOrigin(mergerName + " " + split[1]);
			return infoVo;
		}).collect(Collectors.toList());
		PageVo vo = PageVo.getPage(page);
		return Result.ok().put("data", list).put("page", vo);
	}


	/**
	 * 批量更新订单的状态
	 *
	 * @param ids 订单id
	 * @return result
	 */
	@PostMapping("/up/status/{code}")
	public Result batchUpdateOrderStatus(@RequestBody String[] ids, @PathVariable Integer code) {
		OrderStatusEnum statusEnum = OrderStatusEnum.getInstanceByCode(code);
		if (Objects.isNull(statusEnum)) {
			return Result.error("非法操作");
		}
		System.out.println(Arrays.toString(ids));
		boolean flag = orderService.batchUpdateOrderStatus(ids, statusEnum);
		if (flag) {
			return Result.ok();
		} else {
			return Result.error();
		}
	}


	@PostMapping("/confirm/collect")
	public Result confirmCollectOrder(@RequestBody OrderTo to) {
		logger.info("接收到的信息:{}", to);
		OrderStatusEnum statusEnum = OrderStatusEnum.getInstanceByCode(to.getCode());
		if (Objects.isNull(statusEnum)) {
			return Result.error("非法操作");
		}
		// TODO 更新单个订单的状态为已揽件,随后发起支付
		if (orderService.updateOrderStatus(to, statusEnum)) {
			return Result.ok();
		} else {
			return Result.error();
		}
	}


	@PostMapping("/list/recent")
	public Result queryRecentOrders(@RequestBody Condition condition) {
		logger.info("pageVo{}", condition);
		Page<PostOrder> page = orderService.listRecentOrders(condition);
		PageVo vo = PageVo.getPage(page);
		return Result.ok().put("data", page.getRecords()).put("page", vo);
	}

	/**
	 * @param orderId 订单号
	 * @return 根据已有的订单信息计算运费
	 */
	@GetMapping("/post/cost")
	public Result countPostFee(String orderId) {
		Integer cost = orderService.countPostCost(orderId);
		return Result.ok().put("data", cost);
	}

	@PostMapping("/cost/query/v2")
	public Result countPostFee(@RequestBody @Validated({FreightVo.CountCostWithOrderId.class}) FreightVo vo) {
		PostOrder order = orderService.getById(vo.getOrderId());
		String[] info = order.getOrigin().split(",");
		String[] info2 = order.getOrigin().split(",");
		Integer cost = postCostCounter.countCost(vo.getWeight(), info[0], info2[0], vo.getVolume());
		return Result.ok().put("data", cost);
	}


	@PostMapping("/cost/query/v1")
	public Result countCost(@RequestBody @Validated({FreightVo.CountCostWithoutOrderId.class}) FreightVo vo) {
		// TODO 做好城市代码和邮编的兼容性处理
		String origin = vo.getOrigin();
		origin = areaService.codeExists(origin);
		logger.info("始发地编码{}", origin);
		if (origin == null) {
			return Result.error("非法操作");
		}
		Integer cost = postCostCounter.countCost(vo.getWeight(), origin, vo.getDest(), vo.getVolume());
		return Result.ok().put("data", cost);
	}


	@PostMapping("/association/trace")
	public Result associationOrderWithRoutePath(@RequestBody List<TraceTo> tos) {
		boolean flag = orderService.batchUpdateOrderTraceInfo(tos);
		if (flag) {
			return Result.ok();
		}
		return Result.error();
	}

	/**
	 * @param id traceId
	 * @return 返回订单详情并更新订单的状态为派件中
	 */
	@PostMapping("/dispatch/{id}")
	public Result dispatchOrder(@PathVariable Integer id) {
		List<PostOrder> orders = orderService.batchQueryOrderDetailAndUpdateStatus(id);
		List<PostOrderTo> tos = orders.stream().map(e -> {
			PostOrderTo to = new PostOrderTo();
			BeanUtils.copyProperties(e, to);
			String customerId = orderService.queryCustomerIdByOrderId(e.getId());
			Customer customer = customerService.queryCustomerInfoById(customerId);
			to.setPosterName(customer.getCustomerName());
			to.setPosterPhone(customer.getPhone());
			return to;
		}).collect(Collectors.toList());
		logger.info("响应的数据{}", tos);
		return Result.ok().put("data", tos);
	}


	/**
	 * @param id 订单号
	 * @return 更新订单状态为已结束
	 */
	@PostMapping("/confirm/dispatch/{id}")
	public Result orderOver(@PathVariable Long id) {
		boolean res = orderService.confirmOrder(id + "");
		return res ? Result.ok() : Result.error();
	}


	@GetMapping("/query/phone/{id}")
	public Result queryPhone(@PathVariable String id) {
		String phone = orderService.queryPhoneNumByOrderId(id);
		return Result.ok().put("data", phone);
	}


}
