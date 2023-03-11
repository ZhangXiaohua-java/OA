package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.DispatchOrderInfo;
import com.ruoyi.web.vo.ConditionVo;

/**
 * @author ZhangXiaoHua
 * @description 针对表【t_dispatch_order_info(待派件信息表)】的数据库操作Service
 * @createDate 2023-03-06 20:18:34
 */
public interface DispatchOrderInfoService extends IService<DispatchOrderInfo> {


	/**
	 * @param status
	 * @param orderId 订单号
	 * @return
	 */
	boolean updateNotifyStatusByOrderId(String status, String orderId);


	/**
	 * @return 查询所有派件信息, 包括已派件的
	 */
	Page<DispatchOrderInfo> queryAllDispatchOrders(ConditionVo conditionVo);


	/**
	 * @param orderId 订单号
	 * @return 确认订单信息
	 */
	boolean confirmOrder(String orderId);


	/**
	 * @param url     url地址
	 * @param orderId 订单号
	 *                保存签名信息
	 */
	void saveSignInfo(String url, String orderId);

	/**
	 * @param orderId 订单号
	 * @param url     签名验证信息
	 */
	boolean updateSignInfo(String orderId, String url);

	DispatchOrderInfo queryDetailById(String id);


}
