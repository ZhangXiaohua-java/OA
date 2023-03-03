package com.ruoyi.web.message;

import cn.edu.huel.user.vo.OrderVO;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysDeptService;
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


	@Resource
	private ISysDeptService sysDeptService;

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
		// 根据订单号从Redis中查询订单信息
		OrderVO orderVO = (OrderVO) ops.get(RedisConstant.ORDER_PREFIX + dto.getOrderID());
		log.info("从redis中读取到的信息{}", orderVO);
		String countCode = dto.getCountCode();
		String unifiedCode = dto.getUnifiedCode();
		//TODO 根据城市代码和营业网点的唯一识别号查询该营业部的所有员工
		SysDept sysDept = sysDeptService.queryDeptByUnifiedCode(countCode, unifiedCode);
		// TODO 查询该部门下的所有员工
		List<SysUser> users = userService.getRandomTenEmployeesByDeptId(sysDept.getDeptId());
		// TODO 需要设计一个分配任务的算法,保证公平或者有一定的某种倾向将任务分配给哪些员工
		log.info("查询到的员工信息:{}", users);
		SysUser sysUser = users.get(ThreadLocalRandom.current().nextInt(users.size()));
		Long userId = sysUser.getUserId();
		listOps.rightPush(RedisConstant.ASSIGN_TASK_PREFIX + userId, orderVO);
	}




}
