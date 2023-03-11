package com.ruoyi.web.message;

import cn.edu.huel.user.to.PostOrderTo;
import cn.edu.huel.user.vo.OrderVO;
import cn.edu.huel.user.vo.Result;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.constant.RedisConstant;
import com.ruoyi.web.domain.DispatchOrderInfo;
import com.ruoyi.web.domain.OrderTask;
import com.ruoyi.web.feign.FeignRemoteClient;
import com.ruoyi.web.service.DispatchOrderInfoService;
import com.ruoyi.web.service.OrderTaskService;
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
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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


	@Resource
	private OrderTaskService orderTaskService;

	@Resource
	private DispatchOrderInfoService dispatchOrderInfoService;


	private Snowflake snowflake = IdUtil.getSnowflake();

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
		OrderTask orderTask = new OrderTask();
		orderTask.setOrderId(orderVO.getOrderId());
		orderTask.setEmployeeId(sysUser.getUserId());
		orderTask.setEmployeeName(sysUser.getUserName());
		orderTask.setUnifiedCode(unifiedCode);
		orderTask.setStatus("0");
		orderTask.setPosterPhone(orderVO.getPosterPhone());
		orderTask.setPosterName(orderVO.getPosterName());
		orderTask.setConfirmWeight(null);
		orderTask.setConfirmVolume(null);
		orderTask.setConfirmPostCost(null);
		orderTask.setConfirmTime(null);
		orderTask.setCreateTime(new Date());
		orderTask.setId(snowflake.nextId());
		orderTask.setRemark(orderVO.getRemark());
		orderTask.setSourceCountCode(orderVO.getCountyCode());
		orderTask.setAppointmentTime(orderVO.getAppointmentTime());
		orderTask.setPosterDetailAddress(orderVO.getPostCode() + "," + orderVO.getDetailAddress());
		orderTask.setReceiverInfo("收件人: " + orderVO.getReceiverName() + ",联系方式: " + orderVO.getReceiverPhone() + "收件地址: " + orderVO.getReceiveCode() + "," + orderVO.getDispatchDetailAddress());
		orderTaskService.save(orderTask);
		// 暂时取消掉Redis
		//listOps.rightPush(RedisConstant.ASSIGN_TASK_PREFIX + userId, orderVO);
	}


	@RabbitListener(queues = {"dispatch.order.queue"})
	public void consumerDispatchMessage(Message message) {
		byte[] bytes = message.getBody();
		String msg = new String(bytes, StandardCharsets.UTF_8);
		ListOperations<String, Object> ios = redisTemplate.opsForList();
		List<Object> list = ios.range(RedisConstant.ORDER_TRACE_RELATION + msg, 0, -1);
		log.info("消费到派单的消息{}", msg);
		// TODO 通过feign查询订单详情,并将每一个订单封装成DispatchOrderInfo,然后修改订单的状态为派件中...

		String resultStr = feignRemoteClient.dispatchOrder(Integer.parseInt(msg));
		log.info("响应的字符串格式数据{}", resultStr);
		Result result = JSON.parseObject(resultStr, Result.class);
		if (result.getCode() != 200) {
			log.info("远程服务出错了...");
			return;
		}
		List<JSONObject> objects = (List<JSONObject>) (result.getData().get("data"));
		List<PostOrderTo> orders = objects.stream()
				.map(e -> {
					return JSON.parseObject(e.toString(), PostOrderTo.class);
				}).collect(Collectors.toList());

		log.info("远程服务响应的数据{}", orders);
		List<DispatchOrderInfo> dispatchOrderInfos = orders.stream().map(e -> {
			DispatchOrderInfo dispatchOrderInfo = new DispatchOrderInfo();
			dispatchOrderInfo.setOrderId(e.getId());
			dispatchOrderInfo.setStatus("0");
			dispatchOrderInfo.setReceiverName(e.getReceiverName());
			dispatchOrderInfo.setReceiverPhone(e.getReceiverPhone());
			dispatchOrderInfo.setPosterAddress(e.getOrigin());
			dispatchOrderInfo.setReceiverAddress(e.getDest());
			dispatchOrderInfo.setPosterName(e.getPosterName());
			dispatchOrderInfo.setPosterPhone(e.getPosterPhone());
			dispatchOrderInfo.setOrderTime(e.getOrderTime());
			String dest = e.getDest();
			// 根据目的地分析出最近的营业厅
			String unifiedCode = "DP199675";
			String[] info = e.getDest().split(",");
			SysDept sysDept = sysDeptService.selectDeptByUnifiedCode(info[0], unifiedCode);
			SysUser user = new SysUser();
			user.setDeptId(sysDept.getDeptId());
			List<SysUser> sysUsers = userService.selectUserList(user);
			user = sysUsers.get(0);
			String code = generateCode();
			dispatchOrderInfo.setReceiverCode(code);
			// TODO 发送短信
			CompletableFuture.runAsync(() -> {
				Result res = feignRemoteClient.sendSms(e.getReceiverPhone(), code);
				if (res.getCode() == 200) {
					dispatchOrderInfoService.updateNotifyStatusByOrderId("1", e.getId());
				} else {
					dispatchOrderInfoService.updateNotifyStatusByOrderId("0", e.getId());
				}
			});
			dispatchOrderInfo.setUnifiedCode(sysDept.getUnifiedCode());
			dispatchOrderInfo.setEmployeeId(user.getUserId());
			dispatchOrderInfo.setEmployeeName(user.getUserName());
			return dispatchOrderInfo;
		}).collect(Collectors.toList());
		dispatchOrderInfoService.saveBatch(dispatchOrderInfos);
	}


	public String generateCode() {
		int length = 6;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(ThreadLocalRandom.current().nextInt(10));
		}
		return sb.toString();
	}


}
