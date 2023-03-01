package cn.edu.huel.user.service;

import cn.edu.huel.user.domain.Integral;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 顾客积分Service接口
 *
 * @author 张晓华
 * @date 2023-02-18
 */
public interface IIntegralService extends IService<Integral> {


	/**
	 * 增加用户积分
	 *
	 * @param orderId 订单号
	 * @param amount  增加的积分数
	 * @return
	 */
	boolean addIntegral(String orderId, Integer amount);



}
