package cn.edu.huel.user.controller;


import cn.edu.huel.security.vo.Result;
import cn.edu.huel.user.base.constant.OrderStatusEnum;
import cn.edu.huel.user.base.standard.CreateOrder;
import cn.edu.huel.user.base.utils.PostCostCounter;
import cn.edu.huel.user.domain.PostOrder;
import cn.edu.huel.user.message.MessageService;
import cn.edu.huel.user.service.IAreaService;
import cn.edu.huel.user.service.IPostOrderService;
import cn.edu.huel.user.to.OrderTo;
import cn.edu.huel.user.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.utils.bean.BeanUtils;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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


	/**
	 * 直接将校验过的数据全部投递到rabbitMQ异步消费即可
	 *
	 * @param orderVO 客户端提交过来的请求参数
	 * @return result
	 */
	@PostMapping("/create")
	public Result order(@RequestBody @Validated({CreateOrder.class}) OrderVO orderVO) {
		UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		String id = (String) authentication.getPrincipal();
		orderVO.setCustomerId(id);
		messageService.sendOrder(orderVO);
		return Result.ok();
	}


	/**
	 * 查询所有满足条件的订单
	 *
	 * @param conditionVo 查询条件
	 * @return 订单信息
	 */
	@PostMapping("/query/condition")
	public Result orderDetail(@RequestBody ConditionVo conditionVo) {
		System.out.println(conditionVo);
		UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		String customerId = (String) authentication.getPrincipal();
		conditionVo.setCustomerId(customerId);
		List<PostOrder> orders = orderService.queryOrdersByCondition(conditionVo);
		if (ObjectUtils.isEmpty(orders)) {
			return Result.error("没有满足条件的订单");
		} else {
			List<OrderInfoVo> list = orders.stream()
					.map(e -> {
						OrderInfoVo infoVo = new OrderInfoVo();
						BeanUtils.copyProperties(e, infoVo);
						String[] split = infoVo.getDest().split(",");
						String mergerName = areaService.getMergerNameByZipCode(split[0]);
						infoVo.setDest(mergerName + " " + split[1]);
						split = infoVo.getOrigin().split(",");
						mergerName = areaService.getMergerNameByZipCode(split[0]);
						infoVo.setOrigin(mergerName + " " + split[1]);
						return infoVo;
					})
					.collect(Collectors.toList());
			return Result.ok().put("data", list);
		}
	}


	/**
	 * 批量更新订单的状态
	 *
	 * @param ids 订单id
	 * @return result
	 */
	@PostMapping("/up/status/{code}")
	public Result batchUpdateOrderStatus(@RequestBody String[] ids, @PathVariable char code) {
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


	@GetMapping("/list/recent")
	public Result queryRecentOrders(PageVo pageVo) {
		logger.info("pageVo{}", pageVo);
		Page<PostOrder> page = new Page<>(pageVo.getPageNum(), pageVo.getPageSize());
		List<PostOrder> orders = orderService.listRecentOrders(page);
		return Result.ok().put("data", orders);
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
		Integer cost = postCostCounter.countCost(vo.getWeight(), vo.getOrigin(), vo.getDest(), vo.getVolume());
		return Result.ok().put("data", cost);
	}




}
