package com.ruoyi.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.constant.TransportStatusEnum;
import com.ruoyi.web.domain.RoutePath;
import com.ruoyi.web.domain.TransportPlan;
import com.ruoyi.web.domain.TransportPlanDetail;
import com.ruoyi.web.mapper.TransportPlanMapper;
import com.ruoyi.web.service.TransportPlanDetailService;
import com.ruoyi.web.service.TransportPlanService;
import com.ruoyi.web.vo.ConditionVo;
import com.ruoyi.web.vo.TransportPlanVo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

	@Resource
	private ISysUserService sysUserService;

	/**
	 * @param conditionVo 查询条件
	 * @return 根据查询条件查询信息
	 */
	@Override
	public Page<TransportPlan> queryPlansByCondition(ConditionVo conditionVo) {
		LambdaQueryWrapper<TransportPlan> query = new LambdaQueryWrapper<>();
		query.eq(!StringUtils.isEmpty(conditionVo.getId()), TransportPlan::getId, conditionVo.getId());
		if (conditionVo.getRange() != null && conditionVo.getRange().length > 1) {
			query.between(TransportPlan::getCreateTime, conditionVo.getRange()[0], conditionVo.getRange()[1]);
		}
		query.eq(conditionVo.getStatus() != null, TransportPlan::getStatus, conditionVo.getStatus());
		Page<TransportPlan> page = new Page(conditionVo.getPageNum(), conditionVo.getPageNum());
		this.baseMapper.selectPage(page, query);
		page.getRecords().stream()
				.forEach(e -> {
					if (e.getRoutePath() != null && e.getRoutePath().length != 0) {
						RoutePath path = JSON.parseObject(new String(e.getRoutePath(), StandardCharsets.UTF_8), RoutePath.class);
						e.setPath(path);
					}
				});
		return page;
	}


	/**
	 * @param planVo 运输计划详情
	 * @return 创建新的运输计划
	 */
	@Transactional(rollbackFor = {Throwable.class})
	@Override
	public boolean createNewPlan(TransportPlanVo planVo) {
		TransportPlan plan = new TransportPlan();
		LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		plan.setCreateBy(principal.getUserId());
		plan.setCreateTime(new Date());
		plan.setUpdateTime(new Date());
		// TODO  根据员工信息获取出发地信息
		plan.setDestination(planVo.getDestRegionCode());
		plan.setPlanStartTime(planVo.getLeaveTime());
		plan.setPlanReachTime(planVo.getReachTime());
		plan.setTerm(planVo.getTerm() + "");
		plan.setStatus(TransportStatusEnum.NEW.getCode());
		TransportPlanDetail detail = new TransportPlanDetail();
		detail.setExpectCapacity(Long.valueOf(planVo.getCapacity()));
		detail.setTransportType(planVo.getWay() + "");
		detail.setExpectWeight(Long.valueOf(planVo.getWeight()));
		// TODO 查询出员工信息来确定始发地
		SysUser sysUser = sysUserService.getUserByDeptIdAndId(planVo.getDeptId(), planVo.getEmployeeId());
		if (Objects.isNull(sysUser)) {
			throw new RuntimeException("错误的负责人信息");
		}
		// 返回的SysUser的password字段中存储着部门的countCode
		plan.setOrigin(sysUser.getPassword());
		detail.setChargePerson(planVo.getEmployeeId());
		// TODO 成本核算
		detail.setPlanCost(20000L);
		boolean save = transportPlanDetailService.save(detail);
		// TODO 待完善
		plan.setDetailId(detail.getId());
		return this.baseMapper.insert(plan) == 1 && save;
	}


	/**
	 * @return 查询最近的运输计划信息
	 */
	@Override
	public List<TransportPlan> queryRecentTransportPlan() {
		// TODO 查询数据库中所有出发时间晚于当前时间的所有运输计划信息,需要有五个小时的提前量,
		// 出发前三个小时就不再允许追加订单了
		LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.now());
		now = now.minusHours(5);
		// 当前时间三个小时之前的时间
		Date currentDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 7);
		Date future = calendar.getTime();
		LambdaQueryWrapper<TransportPlan> query = new LambdaQueryWrapper<>();
		query.between(TransportPlan::getPlanStartTime, now, future)
				.and(c -> c.eq(TransportPlan::getStatus, TransportStatusEnum.NEW.getCode()));
		return this.baseMapper.selectList(query);
	}


	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 7);
		Date date = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String res = format.format(date);
		System.out.println(res);

	}


	/**
	 * @param id 计划id
	 * @param c  状态值
	 */
	@Override
	public void updatePlanStatus(Integer id, char c) {
		TransportPlan plan = new TransportPlan();
		plan.setId(id);
		plan.setStatus(c);
		this.updateById(plan);
	}

	/**
	 * 更新路线信息
	 *
	 * @param id    计划id
	 * @param bytes 路线信息
	 */
	@Override
	public void assignRoutePath(Integer id, byte[] bytes) {
		TransportPlan plan = null;
		LambdaQueryWrapper<TransportPlan> query = new LambdaQueryWrapper<>();
		query.eq(TransportPlan::getId, id)
				.and(c -> c.eq(TransportPlan::getStatus, TransportStatusEnum.READY.getCode()));
		plan = this.getOne(query);
		Date now = Date.from(LocalDateTime.now().minusHours(5).atZone(ZoneId.systemDefault()).toInstant());
		if (plan == null || plan.getPlanStartTime().before(now)) {
			throw new UnsupportedOperationException("超出时效,不允许修改");
		}
		plan = new TransportPlan();
		plan.setId(id);
		plan.setRoutePath(bytes);
		plan.setStatus(TransportStatusEnum.ASSIGN_ROUTE_PATH.getCode());
		this.updateById(plan);
	}

	/**
	 * @return 所有已经分配过路线的运输计划信息
	 */
	@Override
	public List<TransportPlan> queryRecentHadAssignedRoutePathPlans() {
		LambdaQueryWrapper<TransportPlan> query = new LambdaQueryWrapper<>();
		query.eq(TransportPlan::getStatus, TransportStatusEnum.ASSIGN_ROUTE_PATH.getCode());
		return this.baseMapper.selectList(query);
	}


	@Override
	public List<TransportPlan> queryAllReadyPlans() {
		LambdaQueryWrapper<TransportPlan> query = new LambdaQueryWrapper<>();
		query.eq(TransportPlan::getStatus, TransportStatusEnum.ASSIGN_ROUTE_PATH.getCode())
				.or()
				.eq(TransportPlan::getStatus, TransportStatusEnum.GONE.getCode())
				.and(c -> c.le(TransportPlan::getPlanStartTime, new Date()));
		return this.baseMapper.selectList(query);
	}


}




