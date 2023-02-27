package com.ruoyi.web.message;

import cn.edu.huel.user.domain.Outlets;
import cn.edu.huel.user.vo.OrderVO;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.constant.RedisConstant;
import com.ruoyi.web.feign.FeignRemoteClient;
import com.ruoyi.web.to.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author 张晓华
 * @date 2023-2-27
 */
@Slf4j
@Component
public class DispatchOrderMessageService {


	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	private ValueOperations<String, Object> ops;

	@Resource
	private FeignRemoteClient feignRemoteClient;

	@Resource
	private ISysUserService userService;

	private ListOperations<String, Object> listOps;

	@PostConstruct
	public void init() {
		ops = redisTemplate.opsForValue();
		listOps = redisTemplate.opsForList();
	}


	@RabbitListener(queues = {"order.dispatch"})
	public void dispatchOrder(Message message) {
		String info = new String(message.getBody(), StandardCharsets.UTF_8);
		OrderDTO dto = JSON.parseObject(info, OrderDTO.class);
		System.out.println(dto);
		OrderVO orderVO = (OrderVO) ops.get(RedisConstant.ORDER_PREFIX + dto.getOrderID());
		log.info("从redis中读取到的信息{}", orderVO);
		String zipCode = dto.getZipCode();
		List<Outlets> outlets = feignRemoteClient.queryOutletsByZipcode(zipCode);
		log.info("查询到的网点信息: {}", outlets);
		Outlets outlet = outlets.get(0);
		Integer outletId = outlet.getId();
		SysUser user = new SysUser();
		user.setOutletsId(outletId);
		List<SysUser> users = userService.selectUserList(user);
		log.info("查询到的员工信息:{}", users);
		SysUser sysUser = users.get(ThreadLocalRandom.current().nextInt(users.size()));
		Long userId = sysUser.getUserId();
		orderVO.setOrderId(dto.getOrderID());
		listOps.rightPush(RedisConstant.ASSIGN_TASK_PREFIX + userId, orderVO);
	}


}
