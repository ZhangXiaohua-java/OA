package cn.edu.huel.user.base.constant;

/**
 * @author 张晓华
 * @date 2023-2-27
 */
public final class RedisConstant {

	/* 订单token,防止用户重复提交订单 */
	public static final String ORDER_TOKEN = "order:token:";

	/* 订单信息前缀*/
	public static final String ORDER_PREFIX = "order:info:";

	/* 订单支付金额的前缀,用于支付时信息的校验 + 订单号 */
	public static final String ORDER_PAY_INFO_PREFIX = "order:pay:";

	/* 订单支付token,用于防止重复提交 + 用户id */
	public static final String PAY_TOKEN = "order:pay:";

	public static final String TRACE_INFO_PREFIX = "trace:info:";


	/* 省份信息 */
	public static final String REGION_PROVINCE_INFO_PREFIX = "region:province:info:";

	/* 地级市信息key */
	public static final String REGION_INFO_PREFIX = "region:info:";

	/* 地级市的所有区县key  */
	public static final String REGION_CHILD_INFO_PREFIX = "region:info:child:";


	/* 转运场信息key */
	public static final String TRANSFER_INFO_PREFIX = "transfer:factory:info:";


}
