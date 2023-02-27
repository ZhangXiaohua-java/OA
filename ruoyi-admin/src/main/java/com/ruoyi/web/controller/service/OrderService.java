package com.ruoyi.web.controller.service;

import cn.edu.huel.user.vo.OrderVO;
import com.ruoyi.web.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
				.collect(Collectors.toList());
		if (list.isEmpty()) {
			log.info("删除了所有的任务");
			redisTemplate.delete(RedisConstant.ASSIGN_TASK_PREFIX + userId);
		} else {
			log.info("过滤之后的任务{}", list);
			ops.rightPushAll(RedisConstant.ASSIGN_TASK_PREFIX + userId, list);
		}
	}


}
