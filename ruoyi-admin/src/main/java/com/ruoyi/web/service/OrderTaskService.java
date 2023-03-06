package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.constant.OrderTaskEnum;
import com.ruoyi.web.domain.OrderTask;

import java.util.List;
import java.util.Set;

/**
 * @author ZhangXiaoHua
 * @description 针对表【t_order_task(任务清单表)】的数据库操作Service
 * @createDate 2023-03-06 20:18:34
 */
public interface OrderTaskService extends IService<OrderTask> {


	/**
	 * @return 任务清单
	 */
	List<OrderTask> listEmployeeTasks();


	/**
	 * @return 带揽件的任务
	 */
	List<OrderTask> listOrdersToCollect();

	/**
	 * @param ids           订单号
	 * @param orderTaskEnum 状态枚举
	 * @return 批量更新任务状态
	 */
	boolean batchUpdateTaskStatusByOrderIds(List<String> ids, OrderTaskEnum orderTaskEnum);


	/**
	 * @param orderId 订单id
	 * @return 任务详情
	 */
	OrderTask queryTaskDetail(String orderId);

	/**
	 * @param orderId       订单号
	 * @param weight        重量
	 * @param volume 体积
	 */
	void updateTaskDetail(String orderId, Integer weight, Integer volume);

	/**
	 *
	 * @param ids 订单号
	 * @return 任务单
	 */
	List<OrderTask> listTasksByOrderIds(Set<String> ids);

}
