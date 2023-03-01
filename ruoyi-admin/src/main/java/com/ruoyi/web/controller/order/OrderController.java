package com.ruoyi.web.controller.order;

import cn.edu.huel.user.vo.OrderVO;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.web.controller.service.OrderService;
import com.ruoyi.web.feign.FeignRemoteClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author 张晓华
 * @date 2023-2-27
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {


	@Resource
	private OrderService orderService;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;


	@Resource
	private FeignRemoteClient remoteClient;

	public static class OrderStatus {

		public static final char CONFIRMED = '2';

		public static final char CREATED = '3';

	}


	/**
	 * 查看任务清单
	 *
	 * @param pageNum  页码
	 * @param pageSize 每页的数据条目数
	 * @return
	 */
	@GetMapping("/task")
	public AjaxResult listTasks(Integer pageNum, @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
		AjaxResult ajaxResult = new AjaxResult(200, "ok");
		LoginUser authentication = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long userId = authentication.getUserId();
		List<OrderVO> vos = orderService.listTasks(userId, pageNum, pageSize);
		return ajaxResult.put("data", vos);
	}


	/**
	 * 批量接收订单
	 *
	 * @param orderIds 订单id
	 * @return
	 */
	@PostMapping("/confirm")
	public AjaxResult confirmOrders(@RequestBody String[] orderIds) {
		List<String> ids = Arrays.asList(orderIds)
				.stream()
				.distinct()
				.collect(Collectors.toList());
		LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		orderService.removeTasksByOrderIds(ids, principal.getUserId());
		String res = remoteClient.batchUpdateOrderStatus(orderIds, OrderStatus.CONFIRMED);
		log.info("远程服务的响应结果{}", res);
		// 收件通知,将订单尾号后六位发送给员工即可
		// TODO 自定义线程池
		CompletableFuture.runAsync(() -> {
			remoteClient.sendSms(principal.getUser().getPhonenumber(), orderIds[0]);
		});
		CompletableFuture.runAsync(() -> {
			orderService.addCollectTasks(orderIds, principal.getUserId());
		});
		return new AjaxResult(200, "ok");
	}

	/**
	 * 查询待揽件清单
	 *
	 * @param pageNum  页码
	 * @param pageSize 每页数据条目数
	 * @return
	 */
	@GetMapping("/collect/list")
	public AjaxResult listGoodsToCollect(@RequestParam(required = false, defaultValue = "1") Long pageNum,
										 @RequestParam(required = false, defaultValue = "10") Long pageSize) {
		LoginUser authentication = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long userId = authentication.getUserId();
		List<OrderVO> vos = orderService.listOrdersToCollect(userId, pageNum, pageSize);
		return new AjaxResult(200, "ok").put("data", vos);
	}

	/**
	 * 确认揽件
	 *
	 * @param orderId 订单id
	 * @return
	 */
	@PutMapping("/confirm/collect")
	public AjaxResult confirmCollectOrder(@RequestParam String orderId) {
		LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.info("订单{}已经被揽收了", orderId);
		boolean result = orderService.confirmCollectOrder(orderId, principal.getUserId());
		CompletableFuture.runAsync(() -> {
			remoteClient.confirmCollectOrder(orderId, OrderStatus.CREATED, principal.getUserId() + "." + principal.getUser().getUserName());
		});
		if (result) {
			return new AjaxResult(200, "ok");
		} else {
			return new AjaxResult(500, "fail");
		}
	}




}
