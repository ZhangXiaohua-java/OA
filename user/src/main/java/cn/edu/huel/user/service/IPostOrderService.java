package cn.edu.huel.user.service;

import cn.edu.huel.user.base.constant.OrderStatusEnum;
import cn.edu.huel.user.domain.PostOrder;
import cn.edu.huel.user.vo.ConditionVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 订单Service接口
 *
 * @author 张晓华
 * @date 2023-02-18
 */
public interface IPostOrderService extends IService<PostOrder> {


	/**
	 * 创建订单信息
	 *
	 * @param postOrder 基础订单信息
	 * @return 结果
	 */
	boolean createOrder(PostOrder postOrder);


	/**
	 * 查询用户的订单详情信息
	 *
	 * @param id        订单id
	 * @param principal 用户id
	 * @return 订单详情
	 */
	List<PostOrder> queryOrderDetail(String id, String principal);


	/**
	 * 根据条件查询订单
	 *
	 * @param conditionVo 条件vo
	 * @return 查询到的用户订单
	 */
	List<PostOrder> queryOrdersByCondition(ConditionVo conditionVo);


	/**
	 * 批量更新订单状态
	 *
	 * @param ids             订单id
	 * @param orderStatusEnum 订单状态
	 * @return 更新结果
	 */
	boolean batchUpdateOrderStatus(String[] ids, OrderStatusEnum orderStatusEnum);


}
