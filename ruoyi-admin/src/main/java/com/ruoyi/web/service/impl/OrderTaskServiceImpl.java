package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.web.constant.OrderTaskEnum;
import com.ruoyi.web.domain.OrderTask;
import com.ruoyi.web.mapper.OrderTaskMapper;
import com.ruoyi.web.service.OrderTaskService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author ZhangXiaoHua
 * @description 针对表【t_order_task(任务清单表)】的数据库操作Service实现
 * @createDate 2023-03-06 20:18:34
 */
@Service
public class OrderTaskServiceImpl extends ServiceImpl<OrderTaskMapper, OrderTask>
		implements OrderTaskService {


	@Override
	public List<OrderTask> listEmployeeTasks() {
		LambdaQueryWrapper<OrderTask> query = new LambdaQueryWrapper<>();
		LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		query.eq(OrderTask::getEmployeeId, user.getUserId())
				.eq(OrderTask::getStatus, OrderTaskEnum.NEW.getCode());
		return this.baseMapper.selectList(query);
	}


	/**
	 * @return 带揽件的任务
	 */
	@Override
	public List<OrderTask> listOrdersToCollect() {
		LoginUser authentication = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long userId = authentication.getUserId();
		LambdaQueryWrapper<OrderTask> query = new LambdaQueryWrapper<>();
		query.eq(OrderTask::getEmployeeId, userId)
				.eq(OrderTask::getStatus, OrderTaskEnum.CONFIRMED.getCode());
		return this.baseMapper.selectList(query);
	}


	/**
	 * @param ids 订单号
	 * @return 批量更新任务状态
	 */
	@Override
	public boolean batchUpdateTaskStatusByOrderIds(List<String> ids, OrderTaskEnum orderTaskEnum) {
		LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		LambdaUpdateWrapper<OrderTask> query = new LambdaUpdateWrapper<>();
		query.in(OrderTask::getOrderId, ids)
				.eq(OrderTask::getStatus, OrderTaskEnum.NEW.getCode())
				.eq(OrderTask::getEmployeeId, user.getUserId());
		query.set(OrderTask::getStatus, orderTaskEnum.getCode());
		return this.update(query);
	}


	/**
	 * @param orderId 订单id
	 * @return 任务详情
	 */
	@Override
	public OrderTask queryTaskDetail(String orderId) {
		LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		LambdaQueryWrapper<OrderTask> query = new LambdaQueryWrapper<>();
		query.eq(OrderTask::getOrderId, orderId)
				.eq(OrderTask::getEmployeeId, user.getUserId())
				.eq(OrderTask::getStatus, OrderTaskEnum.CONFIRMED.getCode());
		return this.baseMapper.selectOne(query);
	}

	/**
	 * @param orderId 订单号
	 * @param weight  重量
	 * @param volume  体积
	 */
	@Override
	public void updateTaskDetail(String orderId, Integer weight, Integer volume) {
		LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		LambdaUpdateWrapper<OrderTask> query = new LambdaUpdateWrapper<>();
		query.eq(OrderTask::getOrderId, orderId)
				.eq(OrderTask::getEmployeeId, user.getUserId())
				.eq(OrderTask::getStatus, OrderTaskEnum.CONFIRMED.getCode());
		query.set(OrderTask::getStatus, OrderTaskEnum.COLLECTED.getCode())
				.set(OrderTask::getEmployeeName, user.getUser().getUserName());
		query.set(OrderTask::getConfirmTime, new Date());
		query.set(OrderTask::getConfirmWeight, weight)
				.set(OrderTask::getConfirmVolume, volume);
		this.baseMapper.update(null, query);
	}


	/**
	 * @param ids 订单号
	 * @return 任务单
	 */
	@Override
	public List<OrderTask> listTasksByOrderIds(Set<String> ids) {
		LambdaQueryWrapper<OrderTask> query = new LambdaQueryWrapper<>();
		query.in(OrderTask::getOrderId, ids)
				.eq(OrderTask::getStatus, OrderTaskEnum.COLLECTED.getCode());
		return this.baseMapper.selectList(query);
	}


}




