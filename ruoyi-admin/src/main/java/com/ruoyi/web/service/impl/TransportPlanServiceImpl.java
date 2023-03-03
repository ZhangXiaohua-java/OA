package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.web.domain.TransportPlan;
import com.ruoyi.web.domain.TransportPlanDetail;
import com.ruoyi.web.mapper.TransportPlanMapper;
import com.ruoyi.web.service.TransportPlanDetailService;
import com.ruoyi.web.service.TransportPlanService;
import com.ruoyi.web.vo.ConditionVo;
import com.ruoyi.web.vo.TransportPlanVo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author ZhangXiaoHua
 * @description 针对表【t_transport_plan(运输计划表)】的数据库操作Service实现
 * @createDate 2023-03-03 15:37:41
 */
@Service
public class TransportPlanServiceImpl extends ServiceImpl<TransportPlanMapper, TransportPlan>
		implements TransportPlanService {


	@Resource
	private TransportPlanDetailService transportPlanDetailService;

	/**
	 * @param conditionVo 查询条件
	 * @return 根据查询条件查询信息
	 */
	@Override
	public List<TransportPlan> queryPlansByCondition(ConditionVo conditionVo) {
		LambdaQueryWrapper<TransportPlan> query = new LambdaQueryWrapper<>();
		query.eq(!StringUtils.isEmpty(conditionVo.getId()), TransportPlan::getId, conditionVo.getId());
		if (conditionVo.getRange() != null && conditionVo.getRange().length > 1) {
			query.between(TransportPlan::getCreateTime, conditionVo.getRange()[0], conditionVo.getRange()[1]);
		}
		query.eq(conditionVo.getStatus() != null, TransportPlan::getStatus, conditionVo.getStatus());
		Page<TransportPlan> page = new Page(conditionVo.getPageNum(), conditionVo.getPageNum());
		this.baseMapper.selectPage(page, query);
		return page.getRecords();
	}


	/**
	 * @param planVo 运输计划详情
	 * @return 创建新的运输计划
	 */
	@Override
	public boolean createNewPlan(TransportPlanVo planVo) {
		TransportPlan plan = new TransportPlan();
		LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		plan.setCreateBy(principal.getUserId());
		plan.setCreateTime(new Date());
		plan.setUpdateTime(new Date());
		// TODO  根据员工信息获取出发地信息
		plan.setOrigin("475300");
		plan.setDestination(planVo.getDestRegionCode());
		plan.setPlanStartTime(planVo.getLeaveTime());
		plan.setPlanReachTime(planVo.getReachTime());
		plan.setTerm(planVo.getTerm() + "");
		plan.setStatus('1');
		TransportPlanDetail detail = new TransportPlanDetail();
		detail.setCapacity(Long.valueOf(planVo.getCapacity()));
		detail.setTransportType(planVo.getWay() + "");
		detail.setExpectWeight(Long.valueOf(planVo.getWeight()));
		detail.setPlanCost(20000L);
		boolean save = transportPlanDetailService.save(detail);
		// TODO 待完善
		return this.baseMapper.insert(plan) == 1 && save;
	}

}




