package cn.edu.huel.user.service;

import cn.edu.huel.user.base.constant.OrderStatusEnum;
import cn.edu.huel.user.domain.PostOrder;
import cn.edu.huel.user.to.OrderTo;
import cn.edu.huel.user.vo.ConditionVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
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


	/**
	 * @param orderId      订单id
	 * @param statusEnum   订单状态枚举
	 * @param employeeInfo 员工信息
	 * @return 修改订单信息
	 */
	boolean updateOrderStatus(OrderTo to, OrderStatusEnum statusEnum);


	/**
	 * 查询所有未支付的订单
	 *
	 * @param pageNum  页码
	 * @param pageSize 每页展示的数据条目数
	 * @return
	 */
	List<PostOrder> queryAllUnpaiedOrder(Integer pageNum, Integer pageSize);


	/**
	 * 查询用户的最近订单
	 *
	 * @param page 分页信息
	 * @return
	 */
	List<PostOrder> listRecentOrders(Page<PostOrder> page);


	/**
	 * @param orderId 订单号
	 * @return 用户单笔订单的金额
	 */
	BigDecimal queryBill(String orderId);


	/**
	 * 更新订单状态
	 *
	 * @param orderId    订单id
	 * @param statusEnum 状态
	 * @return
	 */
	boolean updateOrderStatus(String orderId, OrderStatusEnum statusEnum);


	/**
	 * 根据订单号查询用户id
	 *
	 * @param orderId 订单号
	 * @return
	 */
	String queryCustomerIdByOrderId(String orderId);


	/**
	 * @param orderId 订单号
	 * @return 返回计算的运费
	 */
	Integer countPostCost(String orderId);
}
