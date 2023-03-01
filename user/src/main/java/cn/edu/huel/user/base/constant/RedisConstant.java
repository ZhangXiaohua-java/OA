package cn.edu.huel.user.base.constant;

/**
 * @author 张晓华
 * @date 2023-2-27
 */
public final class RedisConstant {


	/* 订单信息前缀*/
	public static final String ORDER_PREFIX = "order:info:";

	/* 订单支付金额的前缀,用于支付时信息的校验 + 订单号 */
	public static final String ORDER_PAY_INFO_PREFIX = "order:pay:";

	/* 订单支付token,用于防止重复提交 + 用户id */
	public static final String PAY_TOKEN = "order:pay:";


}
