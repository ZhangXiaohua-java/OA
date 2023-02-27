package cn.edu.huel.user.service.impl;

import cn.edu.huel.user.base.constant.OrderStatusEnum;
import cn.edu.huel.user.domain.PostOrder;
import cn.edu.huel.user.mapper.PostOrderMapper;
import cn.edu.huel.user.service.IPostOrderService;
import cn.edu.huel.user.vo.ConditionVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单Service业务层处理
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@Service
public class PostOrderServiceImpl extends ServiceImpl<PostOrderMapper, PostOrder> implements IPostOrderService {


	/**
	 * 创建订单信息
	 *
	 * @param postOrder 基础订单信息
	 * @return 结果
	 */
	@Override
	public boolean createOrder(PostOrder postOrder) {
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
	public List<PostOrder> queryOrdersByCondition(ConditionVo conditionVo) {
		Page<PostOrder> page = new Page<>(conditionVo.getPageNum(), conditionVo.getPageSize());
		LambdaQueryWrapper<PostOrder> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(!StringUtils.isEmpty(conditionVo.getCustomerId()),
						PostOrder::getCustomerId, conditionVo.getCustomerId())
				.and(!ObjectUtils.isEmpty(conditionVo.getRange()), (c) -> {
					c.between(PostOrder::getOrderTime, conditionVo.getRange()[0], conditionVo.getRange()[1]);
				});
		this.baseMapper.selectPage(page, wrapper);
		return page.getRecords();
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
		List<PostOrder> orders = Arrays.stream(ids)
				.map(e -> {
					PostOrder order = new PostOrder();
					order.setId(e);
					order.setStatus(orderStatusEnum.getCode());
					return order;
				}).collect(Collectors.toList());
		return this.updateBatchById(orders);
	}




}
