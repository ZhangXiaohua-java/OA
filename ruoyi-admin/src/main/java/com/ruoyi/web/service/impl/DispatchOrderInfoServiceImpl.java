package com.ruoyi.web.service.impl;

import cn.edu.huel.user.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.web.domain.DispatchOrderInfo;
import com.ruoyi.web.feign.FeignRemoteClient;
import com.ruoyi.web.mapper.DispatchOrderInfoMapper;
import com.ruoyi.web.service.DispatchOrderInfoService;
import com.ruoyi.web.vo.ConditionVo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * @author ZhangXiaoHua
 * @description 针对表【t_dispatch_order_info(待派件信息表)】的数据库操作Service实现
 * @createDate 2023-03-06 20:18:34
 */
@Service
public class DispatchOrderInfoServiceImpl extends ServiceImpl<DispatchOrderInfoMapper, DispatchOrderInfo> implements DispatchOrderInfoService {


	@Resource
	private FeignRemoteClient feignRemoteClient;

	@Resource
	private ISysDeptService sysDeptService;


	/**
	 * @param status
	 * @param orderId 订单号
	 * @return
	 */
	@Override
	public boolean updateNotifyStatusByOrderId(String status, String orderId) {
		LambdaUpdateWrapper<DispatchOrderInfo> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.eq(DispatchOrderInfo::getOrderId, orderId);
		updateWrapper.set(DispatchOrderInfo::getNotifyStatus, status);
		return this.update(updateWrapper);
	}


	/**
	 * @return 查询所有派件信息, 包括已派件的
	 */
	@Override
	public Page<DispatchOrderInfo> queryAllDispatchOrders(ConditionVo conditionVo) {
		LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long deptId = loginUser.getUser().getDeptId();
		SysDept sysDept = sysDeptService.selectDeptById(deptId);
		Page<DispatchOrderInfo> page = new Page<>((conditionVo.getPageNum() - 1L) * conditionVo.getPageSize(), conditionVo.getPageSize());
		LambdaQueryWrapper<DispatchOrderInfo> query = new LambdaQueryWrapper<>();
		query.eq(DispatchOrderInfo::getUnifiedCode, sysDept.getUnifiedCode());
		Optional.ofNullable(conditionVo.getId()).ifPresent(e -> {
			if (!"".equals(e)) {
				query.like(DispatchOrderInfo::getOrderId, e).or().like(DispatchOrderInfo::getReceiverPhone, e).or().like(DispatchOrderInfo::getReceiverName, e);
			}
		});
		Optional.ofNullable(conditionVo.getDateRange()).ifPresent(e -> {
			query.and(ele -> ele.between(DispatchOrderInfo::getCreateTime, e[0], e[1]));
		});
		return this.baseMapper.selectPage(page, query);
	}


	/**
	 * @param orderId 订单号
	 * @return 确认订单信息
	 */
	@Override
	public boolean confirmOrder(String orderId) {
		LambdaUpdateWrapper<DispatchOrderInfo> update = new LambdaUpdateWrapper<>();
		update.eq(DispatchOrderInfo::getOrderId, orderId).eq(DispatchOrderInfo::getStatus, "0");
		update.set(DispatchOrderInfo::getStatus, "1");
		update.set(DispatchOrderInfo::getConfirmTime, new Date());
		boolean res = this.update(update);
		LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = user.getUser().getUserName();
		Long userId = user.getUser().getUserId();
		Result result = null;
		if (res) {
			// TODO 发起远程调用更新订单状态为已结束
			result = feignRemoteClient.orderOver(Long.valueOf(orderId), userName, userId + "");
		}
		return res && result.getCode() == 200;
	}

	/**
	 * @param url     url地址
	 * @param orderId 订单号
	 *                保存签名信息
	 */
	@Override
	public void saveSignInfo(String url, String orderId) {
		LambdaUpdateWrapper<DispatchOrderInfo> update = new LambdaUpdateWrapper<>();
		update.eq(DispatchOrderInfo::getOrderId, orderId).eq(DispatchOrderInfo::getStatus, "1");
		update.set(DispatchOrderInfo::getSign, url);
	}


	/**
	 * @param orderId 订单号
	 * @param url     签名验证信息
	 */
	@Override
	public boolean updateSignInfo(String orderId, String url) {
		LambdaUpdateWrapper<DispatchOrderInfo> update = new LambdaUpdateWrapper<>();
		update.eq(DispatchOrderInfo::getOrderId, orderId);
		update.set(DispatchOrderInfo::getSign, url);
		return this.update(update);
	}

	@Override
	public DispatchOrderInfo queryDetailById(String id) {
		LambdaQueryWrapper<DispatchOrderInfo> query = new LambdaQueryWrapper<>();
		query.eq(DispatchOrderInfo::getOrderId, id);
		return this.baseMapper.selectOne(query);
	}


}




