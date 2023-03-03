package com.ruoyi.web.controller.service;

import cn.edu.huel.user.vo.OrderVO;
import com.ruoyi.web.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 张晓华
 * @date 2023-2-27
 */
@Slf4j
@Service
public class OrderService {

	@Resource
	private RedisTemplate<String, OrderVO> redisTemplate;

	ListOperations<String, OrderVO> ops;

	@PostConstruct
	public void init() {
		ops = redisTemplate.opsForList();
	}

	public List<OrderVO> listTasks(Long userId, Integer pageNum, Integer pageSize) {
		return ops.range(RedisConstant.ASSIGN_TASK_PREFIX + userId, (pageNum - 1) * pageSize, pageSize);
	}


	public void removeTasksByOrderIds(List<String> ids, Long userId) {
		List<OrderVO> range = ops.range(RedisConstant.ASSIGN_TASK_PREFIX + userId, 0, -1);
		List<OrderVO> list = range.stream()
				.filter(e -> !ids.contains(e.getOrderId()))
				.distinct()
				.collect(Collectors.toList());
		if (list.isEmpty()) {
			log.info("删除了所有的任务");
			redisTemplate.delete(RedisConstant.ASSIGN_TASK_PREFIX + userId);
		} else {
			log.info("过滤之后的任务{}", list);
			redisTemplate.delete(RedisConstant.ASSIGN_TASK_PREFIX + userId);
			ops.rightPushAll(RedisConstant.ASSIGN_TASK_PREFIX + userId, list);
		}
	}


	/**
	 * 将确认过的订单后统一分配到员工的待揽件任务清单中去
	 *
	 * @param ids    订单id
	 * @param userId 员工id
	 * @return result
	 */
	public boolean addCollectTasks(String[] ids, Long userId) {
		List<OrderVO> vos = Arrays.stream(ids)
				.map(e -> {
					OrderVO vo = new OrderVO();
					vo.setOrderId(e);
					return vo;
				})
				.collect(Collectors.toList());
		BoundListOperations<String, OrderVO> operations = redisTemplate.boundListOps(RedisConstant.ORDER_TO_COLLECT_ID_PREFIX + userId);
		operations.rightPushAll(vos.toArray(new OrderVO[]{}));
		return true;
	}


	public List<OrderVO> listOrdersToCollect(Long userId, Long pageNum, Long pageSize) {
		BoundListOperations<String, OrderVO> ops = redisTemplate.boundListOps(RedisConstant.ORDER_TO_COLLECT_ID_PREFIX + userId);
		List<OrderVO> vos = ops.range((pageNum - 1) * pageSize, pageSize);
		List<String> ids = vos.stream()
				.map(e -> e.getOrderId()).collect(Collectors.toList());
		ValueOperations<String, OrderVO> valueOperations = redisTemplate.opsForValue();
		return ids.stream()
				.map(e -> valueOperations.get(RedisConstant.ORDER_PREFIX + e))
				.collect(Collectors.toList());
	}


	public boolean confirmCollectOrder(String orderId, Long userId) {
		BoundListOperations<String, OrderVO> listOps = redisTemplate.boundListOps(RedisConstant.ORDER_TO_COLLECT_ID_PREFIX + userId);
		OrderVO vo = new OrderVO();
		vo.setOrderId(orderId);
		Long remove = listOps.remove(1, vo);
		return !Long.valueOf(0).equals(remove);
	}


}
