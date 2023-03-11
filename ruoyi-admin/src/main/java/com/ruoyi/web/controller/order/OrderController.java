package com.ruoyi.web.controller.order;

import cn.edu.huel.user.to.OrderTo;
import cn.edu.huel.user.vo.Result;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.web.constant.OrderTaskEnum;
import com.ruoyi.web.constant.RedisConstant;
import com.ruoyi.web.controller.service.OrderService;
import com.ruoyi.web.domain.OrderTask;
import com.ruoyi.web.feign.FeignRemoteClient;
import com.ruoyi.web.service.OrderTaskService;
import com.ruoyi.web.vo.ConditionVo;
import com.ruoyi.web.vo.OrderVo;
import com.ruoyi.web.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
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
	private OrderTaskService orderTaskService;


	@Resource
	private FeignRemoteClient remoteClient;

	@Resource
	private Executor executor;

	private volatile DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static class OrderStatus {

		public static final Integer CONFIRMED = 2;

		public static final Integer CREATED = 3;

	}


	/**
	 * 查看任务清单
	 *
	 * @param pageNum  页码
	 * @param pageSize 每页的数据条目数
	 * @return
	 */
	@PostMapping("/task")
	public AjaxResult listTasks(@RequestBody ConditionVo conditionVo) {
		AjaxResult ajaxResult = new AjaxResult(200, "ok");
		LoginUser authentication = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long userId = authentication.getUserId();
		Page<OrderTask> page = orderTaskService.listEmployeeTasks(conditionVo);
		PageVo vo = PageVo.getPage(page);
		return ajaxResult.put("data", page.getRecords()).put("page", vo);
	}


	/**
	 * 批量接收订单
	 *
	 * @param orderIds 订单id
	 * @return
	 */
	@PostMapping("/confirm")
	public AjaxResult confirmOrders(@RequestBody String[] orderIds) {
		List<String> ids = Arrays.asList(orderIds).stream().distinct().collect(Collectors.toList());
		LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// 更新user服务中订单的状态
		String res = remoteClient.batchUpdateOrderStatus(orderIds, OrderStatus.CONFIRMED);
		// 移除Redis中的任务信息
		//orderService.removeTasksByOrderIds(ids, principal.getUserId());
		log.info("远程服务的响应结果{}", res);
		// 收件通知,将订单尾号后六位发送给员工即可
		// TODO 自定义线程池,发送收件提醒信息
		ids.stream().forEach(e -> {
			CompletableFuture.runAsync(() -> {
				// TODO 根据订单号查询下单用户的手机号信息
				// TODO 这里可能就是不同包导致的Json解析的bug
				Result result = remoteClient.queryPhoneNum(e);
				result = JSON.parseObject(JSON.toJSONString(result), Result.class);
				if (result.getCode() != 200) {
					log.warn("订单号为{}的手机号查询活动失败", e);
					return;
				}
				String phone = (String) result.getData().get("data");
				result = remoteClient.sendSms(phone, e.substring(13));
				log.info("手机号为{}的通知短信的发送结果{}", phone, result);
			}, executor);
		});
		boolean result = orderTaskService.batchUpdateTaskStatusByOrderIds(ids, OrderTaskEnum.CONFIRMED);
		// 添加揽件任务
		//CompletableFuture.runAsync(() -> {
		//	orderService.addCollectTasks(orderIds, principal.getUserId());
		//});
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
	public AjaxResult listGoodsToCollect(ConditionVo conditionVo) {
		LoginUser authentication = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long userId = authentication.getUserId();
		Page<OrderTask> page = orderTaskService.listOrdersToCollect(conditionVo);
		//List<OrderVO> vos = orderService.listOrdersToCollect(userId, pageNum, pageSize);
		PageVo vo = PageVo.getPage(page);
		return new AjaxResult(200, "ok").put("data", page.getRecords()).put("page", vo);
	}

	/**
	 * 确认揽件
	 *
	 * @param orderId 订单id
	 * @param weight  重量
	 * @param volume  体积
	 * @return
	 */
	@PostMapping("/confirm/collect")
	public AjaxResult confirmCollectOrder(@RequestBody OrderVo orderVO) {
		log.info("订单号{}重量为{}KG,体积为{}立方厘米", orderVO.getOrderId(), orderVO.getWeight(), orderVO.getVolume());
		LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.info("订单{}已经被揽收了", orderVO.getOrderId());
		OrderTask task = orderTaskService.queryTaskDetail(orderVO.getOrderId());
		if (Objects.isNull(task)) {
			return new AjaxResult(500, "fail");
		}
		orderTaskService.updateTaskDetail(orderVO.getOrderId(), orderVO.getWeight(), orderVO.getVolume());
		String countyCode = task.getSourceCountCode();
		String timeStr = dateTimeFormatter.format(LocalDate.now());
		BoundSetOperations<String, Object> ops = redisTemplate.boundSetOps(RedisConstant.REGION_ORDER_TRANSPORT_PREFIX + timeStr + ":" + countyCode);
		// TODO  订单确认之后统一将订单信息加入到区域性的转运场集合的待运输的订单集合中去,后续的运输计划会处理这些订单
		//boolean result = orderService.confirmCollectOrder(orderVO.getOrderId(), principal.getUserId());
		ops.add(orderVO.getOrderId());
		CompletableFuture.runAsync(() -> {
			OrderTo to = new OrderTo();
			BeanUtils.copyProperties(orderVO, to);
			to.setCode(OrderStatus.CREATED);
			to.setEmployee(principal.getUserId() + "." + principal.getUser().getUserName());
			remoteClient.confirmCollectOrder(to);
		});
		return new AjaxResult(200, "ok");
	}


}
