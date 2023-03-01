package com.ruoyi.web.constant;

/**
 * @author 张晓华
 * @date 2023-2-27
 */
public final class RedisConstant {


	/* Redis中订单信息的前缀 */
	public static final String ORDER_PREFIX = "order:info:";

	/* Redis中分配的订单任务的信息前缀 */
	public static final String ASSIGN_TASK_PREFIX = "employee:tasks:";

	/* Redis中员工待揽件的订单号前缀 */
	public static final String ORDER_TO_COLLECT_ID_PREFIX = "order:collect:";

}
