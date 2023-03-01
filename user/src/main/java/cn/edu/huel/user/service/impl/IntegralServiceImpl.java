package cn.edu.huel.user.service.impl;

import cn.edu.huel.user.domain.Integral;
import cn.edu.huel.user.mapper.IntegralMapper;
import cn.edu.huel.user.service.IIntegralService;
import cn.edu.huel.user.service.IPostOrderService;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * 顾客积分Service业务层处理
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@Service
public class IntegralServiceImpl extends ServiceImpl<IntegralMapper, Integral> implements IIntegralService {

	@Resource
	private IPostOrderService postOrderService;


	/**
	 * 增加用户积分
	 *
	 * @param orderId 订单号
	 * @param amount  增加的积分数
	 * @return
	 */
	@Override
	public boolean addIntegral(String orderId, Integer amount) {
		String customerId = postOrderService.queryCustomerIdByOrderId(orderId);
		Integral integral = this.baseMapper.queryLastIntegral(customerId);
		// 如果该用户在此之前没有消费过,则直接插入一条记录即可
		// 否则就在之前的基础上叠加积分
		if (Objects.isNull(integral)) {
			integral = new Integral();
			integral.setCurrentIntegral(Long.valueOf(amount));
			integral.setLastIntegral(0L);
			integral.setCustomerId(customerId);
			integral.setUpdateTime(new Date());
			integral.setOrderId(orderId);
			integral.setId(IdUtil.nanoId());
			return this.baseMapper.insert(integral) == 1;
		} else {
			Integral newIntegral = new Integral();
			newIntegral.setId(IdUtil.nanoId());
			newIntegral.setCurrentIntegral(integral.getCurrentIntegral() + amount);
			newIntegral.setCustomerId(customerId);
			newIntegral.setOrderId(orderId);
			newIntegral.setLastIntegral(integral.getCurrentIntegral());
			newIntegral.setUpdateTime(new Date());
			return this.baseMapper.insert(newIntegral) == 1;
		}
	}


}
