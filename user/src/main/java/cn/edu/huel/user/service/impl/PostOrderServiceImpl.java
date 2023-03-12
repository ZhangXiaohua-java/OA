package cn.edu.huel.user.service.impl;

import cn.edu.huel.user.base.constant.OrderStatusEnum;
import cn.edu.huel.user.base.utils.PostCostCounter;
import cn.edu.huel.user.domain.OrderTrace;
import cn.edu.huel.user.domain.PostOrder;
import cn.edu.huel.user.mapper.PostOrderMapper;
import cn.edu.huel.user.service.IAreaService;
import cn.edu.huel.user.service.IOrderTraceService;
import cn.edu.huel.user.service.IPostOrderService;
import cn.edu.huel.user.to.OrderTo;
import cn.edu.huel.user.to.TraceTo;
import cn.edu.huel.user.vo.Condition;
import cn.edu.huel.user.vo.ConditionVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单Service业务层处理
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@Service
public class PostOrderServiceImpl extends ServiceImpl<PostOrderMapper, PostOrder> implements IPostOrderService {


	@Resource
	private IAreaService areaService;


	@Resource
	private PostCostCounter postCostCounter;


	@Resource
	private IOrderTraceService orderTraceService;

	/**
	 * 创建订单信息
	 *
	 * @param postOrder 基础订单信息
	 * @return 结果
	 */
	@Override
	public boolean createOrder(PostOrder postOrder) {
		postOrder.setUpdateTime(new Date());
		int insert = this.baseMapper.insert(postOrder);
		return insert == 1;
	}


	/**
	 * 查询用户的订单详情信息
	 *
	 * @param id        订单id
	 * @param principal 用户id
	 * @return 订单详情
	 */
	@Override
	public List<PostOrder> queryOrderDetail(String id, String principal) {
		LambdaQueryWrapper<PostOrder> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(PostOrder::getCustomerId, principal)
				.eq(PostOrder::getId, id);
		return this.baseMapper.selectList(wrapper);
	}


	/**
	 * 根据条件查询订单
	 *
	 * @param conditionVo 条件vo
	 * @return 查询到的用户订单
	 */
	@Override
	@SuppressWarnings({"all"})
	public Page<PostOrder> queryOrdersByCondition(Condition condition) {
		UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		String customerId = (String) authentication.getPrincipal();
		Page<PostOrder> page = new Page<>(condition.getPageNum(), condition.getPageSize());
		LambdaQueryWrapper<PostOrder> query = new LambdaQueryWrapper<>();
		query.eq(PostOrder::getCustomerId, customerId);
		Optional.ofNullable(condition.getId())
				.ifPresent(e -> {
					if (!"".equals(e)) {
						query.like(PostOrder::getReceiverName, e)
								.or()
								.like(PostOrder::getId, e)
								.or()
								.like(PostOrder::getReceiverPhone, e);
					}
				});
		Optional.ofNullable(condition.getStatus())
				.ifPresent(e -> {
					query.and(c -> c.eq(PostOrder::getStatus, e));
				});
		Optional.ofNullable(condition.getDateRange())
				.ifPresent(e -> {
					query.and(ele -> ele.between(PostOrder::getOrderTime, e[0], e[1]));
				});
		return this.baseMapper.selectPage(page, query);
	}


	/**
	 * 批量更新订单状态
	 *
	 * @param ids             订单id
	 * @param orderStatusEnum 订单状态
	 * @return 更新结果
	 */
	@Override
	public boolean batchUpdateOrderStatus(String[] ids, OrderStatusEnum orderStatusEnum) {
		boolean flag = false;
		BigDecimal bigDecimal = new BigDecimal("10");
		if (orderStatusEnum.getCode() == '3') {
			// TODO 计算运费
			flag = true;
		}
		boolean needCountPrice = flag;
		List<PostOrder> orders = Arrays.stream(ids)
				.map(e -> {
					PostOrder order = new PostOrder();
					order.setId(e);
					order.setUpdateTime(new Date());
					order.setStatus(orderStatusEnum.getCode());
					if (needCountPrice) {
						order.setPay(bigDecimal);
					}
					return order;
				}).collect(Collectors.toList());
		return this.updateBatchById(orders);
	}


	/**
	 * @param orderId      订单id
	 * @param statusEnum   订单状态枚举
	 * @param employeeInfo 员工信息
	 * @return 修改订单信息
	 */
	@Override
	public boolean updateOrderStatus(OrderTo to, OrderStatusEnum statusEnum) {
		PostOrder postOrder = this.baseMapper.selectById(to.getOrderId());
		PostOrder order = new PostOrder();
		order.setId(to.getOrderId());
		order.setStatus(statusEnum.getCode());
		String[] info = to.getEmployee().split("\\.");
		order.setCreateBy(Long.valueOf(info[0]));
		order.setEmployeeName(info[1]);
		order.setWeight(to.getWeight());
		order.setVolume(to.getVolume());
		Integer cost = postCostCounter.countCost(to.getWeight(), postOrder.getOrigin().split(",")[0], postOrder.getDest().split(",")[0], to.getVolume());
		order.setPay(new BigDecimal(cost));
		order.setUpdateTime(new Date());
		return this.baseMapper.updateById(order) == 1;
	}


	/**
	 * 查询所有未支付的订单
	 *
	 * @param pageNum  页码
	 * @param pageSize 每页展示的数据条目数
	 * @return
	 */
	@Override
	public List<PostOrder> queryAllUnpaiedOrder(Integer pageNum, Integer pageSize) {
		LambdaQueryWrapper<PostOrder> query = new LambdaQueryWrapper<>();
		UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		String id = (String) authentication.getPrincipal();
		query.eq(PostOrder::getCustomerId, id)
				.and(true, c -> c.eq(PostOrder::getStatus, ConditionVo.OrderStatus.NOT_PAY.getCode()));
		Page<PostOrder> page = new Page(pageNum, pageSize);
		this.baseMapper.selectPage(page, query);
		return page.getRecords();
	}


	/**
	 * 查询用户的最近订单
	 *
	 * @param page 分页信息
	 * @return
	 */
	@Override
	public Page<PostOrder> listRecentOrders(Condition condition) {
		Page<PostOrder> page = new Page<>((condition.getPageNum() - 1L) * condition.getPageSize(), condition.getPageSize());
		UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		String userId = (String) authentication.getPrincipal();
		LambdaQueryWrapper<PostOrder> query = new LambdaQueryWrapper<>();
		query.eq(PostOrder::getCustomerId, userId);
		Optional.ofNullable(condition.getId())
				.ifPresent(e -> {
					if (!"".equals(e)) {
						query.like(PostOrder::getReceiverName, e)
								.or()
								.like(PostOrder::getId, e)
								.or()
								.like(PostOrder::getReceiverPhone, e);
					}
				});
		Optional.ofNullable(condition.getStatus())
				.ifPresent(e -> {
					query.and(c -> c.eq(PostOrder::getStatus, e));
				});
		Optional.ofNullable(condition.getDateRange())
				.ifPresent(e -> {
					query.and(ele -> ele.between(PostOrder::getOrderTime, e[0], e[1]));
				});
		this.baseMapper.selectPage(page, query);
		List<PostOrder> list = page.getRecords();
		list.stream()
				.forEach(e -> {
					String[] info = e.getOrigin().split(",");
					String name = areaService.getMergerNameByZipCode(info[0]);
					e.setOrigin(name + " " + info[1]);
					info = e.getDest().split(",");
					name = areaService.getMergerNameByZipCode(info[0]);
					e.setDest(name + " " + info[1]);
				});
		return page;
	}

	/**
	 * @param orderId 订单号
	 * @return 用户单笔订单的金额
	 */
	@Override
	public BigDecimal queryBill(String orderId) {
		UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		String id = (String) authentication.getPrincipal();
		LambdaQueryWrapper<PostOrder> query = new LambdaQueryWrapper<>();
		query.select(PostOrder::getPay)
				.eq(PostOrder::getId, orderId)
				.and((c) -> c.eq(PostOrder::getCustomerId, id));
		return this.baseMapper.selectOne(query).getPay();
	}


	/**
	 * 更新订单状态
	 *
	 * @param orderId    订单id
	 * @param statusEnum 状态
	 * @return
	 */
	@Override
	public boolean updateOrderStatus(String orderId, OrderStatusEnum statusEnum) {
		PostOrder order = this.getById(orderId);
		int pre = 0;
		if (order.getStatus() > 10) {
			pre = order.getStatus() - 10;
			order = new PostOrder();
			order.setId(orderId);
			order.setStatus(pre);
		} else {
			order = new PostOrder();
			order.setId(orderId);
			order.setStatus(statusEnum.getCode());
		}
		order.setUpdateTime(new Date());
		return this.updateById(order);
	}


	/**
	 * 根据订单号查询用户id
	 *
	 * @param orderId 订单号
	 * @return
	 */
	@Override
	public String queryCustomerIdByOrderId(String orderId) {
		LambdaQueryWrapper<PostOrder> query = new LambdaQueryWrapper<>();
		query.select(PostOrder::getCustomerId);
		query.eq(PostOrder::getId, orderId);
		PostOrder order = this.baseMapper.selectOne(query);
		return Objects.isNull(order) ? null : order.getCustomerId();
	}


	/**
	 * @param orderId 订单号
	 * @return 返回计算的运费
	 */
	@Override
	public Integer countPostCost(String orderId) {
		PostOrder order = this.baseMapper.selectById(orderId);
		if (Objects.isNull(order)) {
			return null;
		}
		String[] origin = order.getOrigin().split(",");
		String[] dest = order.getDest().split(",");
		return postCostCounter.countCost(order.getWeight(), origin[0], dest[0], order.getVolume());
	}


	/**
	 * @param tos 数据
	 * @return 批量更新订单关联的追踪信息
	 */
	@Override
	public boolean batchUpdateOrderTraceInfo(List<TraceTo> tos) {
		List<String> orderIds = tos.stream().map(e -> e.getOrderId()).collect(Collectors.toList());
		List<PostOrder> list = this.listByIds(orderIds);
		List<PostOrder> orders = tos.stream()
				.map(e -> {
					PostOrder order = new PostOrder();
					order.setUpdateTime(new Date());
					order.setId(e.getOrderId());
					order.setTraceId(e.getTraceId());
					order.setStatus(OrderStatusEnum.POSTING.getCode());
					// TODO  查询原始的订单的支付状态信息,如果是未支付的订单,则将该订单的状态值在原有基础上+10以表示是未支付的订单
					list.stream().filter(ele -> ele.getId().equals(e.getOrderId()))
							.findAny().ifPresent(consumer -> {
								order.setStatus((order.getStatus() + 10));
							});
					return order;
				}).collect(Collectors.toList());
		List<OrderTrace> traceList = orders.stream().map(e -> {
			OrderTrace trace = new OrderTrace();
			trace.setTraceId(e.getTraceId());
			trace.setOrderId(e.getId());
			trace.setStatus('1');
			trace.setUpdateTime(new Date());
			return trace;
		}).collect(Collectors.toList());
		orderTraceService.saveBatch(traceList);
		return this.updateBatchById(orders);
	}

	/**
	 * @param traceId traceId
	 * @return 订单详情
	 */
	@Override
	public List<PostOrder> batchQueryOrderDetailAndUpdateStatus(Integer traceId) {
		LambdaQueryWrapper<PostOrder> query = new LambdaQueryWrapper<>();
		query.eq(PostOrder::getTraceId, traceId);
		List<PostOrder> orders = this.baseMapper.selectList(query);
		List<PostOrder> update = orders.stream().map(e -> {
			PostOrder order = new PostOrder();
			order.setUpdateTime(new Date());
			order.setId(e.getId());
			order.setStatus(OrderStatusEnum.DISPATCHING.getCode());
			return order;
		}).collect(Collectors.toList());
		this.updateBatchById(update);
		return orders;
	}


	/**
	 * @param orderId      订单号
	 * @param employeeName 员工姓名
	 * @param employeeId   员工id
	 * @return 修改订单状态为已结束
	 */
	@Override
	public boolean confirmOrder(String orderId, String employeeName, String employeeId) {
		LambdaUpdateWrapper<PostOrder> update = new LambdaUpdateWrapper<>();
		update.eq(PostOrder::getId, orderId)
				.ne(PostOrder::getStatus, OrderStatusEnum.Finished.getCode());
		update.set(PostOrder::getStatus, OrderStatusEnum.Finished.getCode());
		update.set(PostOrder::getUpdateTime, new Date());
		update.set(PostOrder::getEmployeeName, employeeName + "," + employeeId);
		return this.update(update);
	}

	/**
	 * @param id 订单号
	 * @return 根据订单号查询下单用户的手机号信息
	 */
	@Override
	public String queryPhoneNumByOrderId(String id) {
		return this.baseMapper.queryCustomerPhoneByOrderId(id);
	}


}
