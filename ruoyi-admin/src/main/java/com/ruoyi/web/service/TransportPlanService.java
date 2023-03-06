package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.TransportPlan;
import com.ruoyi.web.vo.ConditionVo;
import com.ruoyi.web.vo.TransportPlanVo;

import java.util.List;

/**
 * @author ZhangXiaoHua
 * @description 针对表【t_transport_plan(运输计划表)】的数据库操作Service
 * @createDate 2023-03-03 15:37:41
 */
public interface TransportPlanService extends IService<TransportPlan> {


	/**
	 * @param conditionVo 查询条件
	 * @return 根据查询条件查询信息
	 */
	List<TransportPlan> queryPlansByCondition(ConditionVo conditionVo);

	/**
	 * @param planVo 运输计划详情
	 * @return 创建新的运输计划
	 */
	boolean createNewPlan(TransportPlanVo planVo);

	/**
	 * @return 查询最近的运输计划信息
	 */
	List<TransportPlan> queryRecentTransportPlan();


	/**
	 * @param id 计划id
	 * @param c  状态值
	 */
	void updatePlanStatus(Integer id, char c);

	/**
	 * 更新路线信息
	 *
	 * @param id    计划id
	 * @param bytes 路线信息
	 */
	void assignRoutePath(Integer id, byte[] bytes);


	/**
	 * @return 所有已经分配过路线的运输计划信息
	 */
	List<TransportPlan> queryRecentHadAssignedRoutePathPlans();

	List<TransportPlan> queryAllReadyPlans();


}
