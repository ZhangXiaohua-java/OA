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


	@GetMapping("/task")
	public AjaxResult listTasks(Integer pageNum, @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
		AjaxResult ajaxResult = new AjaxResult(200, "ok");
		LoginUser authentication = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long userId = authentication.getUserId();
		List<OrderVO> vos = orderService.listTasks(userId, pageNum, pageSize);
		return ajaxResult.put("data", vos);
	}


	@PostMapping("/confirm")
	public AjaxResult confirmOrders(@RequestBody String[] orderIds) {
		List<String> ids = Arrays.asList(orderIds)
				.stream()
				.distinct()
				.collect(Collectors.toList());
		LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		orderService.removeTasksByOrderIds(ids, principal.getUserId());
		String res = remoteClient.batchUpdateOrderStatus(orderIds);
		log.info("远程服务的响应结果{}", res);
		// TODO 收件通知
		return new AjaxResult(200, "ok");
	}


}
