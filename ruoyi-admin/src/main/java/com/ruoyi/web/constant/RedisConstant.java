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


	/* 规划路径的原始信息*/
	public static final String TRANSPORT_PLAN_ROUTE_PATH_RAW_PREFIX = "transport:route:path:raw:";

	/* 规划路径的简略信息 */
	public static final String TRANSPORT_PLAN_ROUTE_PATH_PREFIX = "transport:route:path:";

	/* 每个区域内所有待运输件的前缀 */
	public static final String REGION_ORDER_TRANSPORT_PREFIX = "order:transport:region:";

	public static final String TRACE_INFO_PREFIX = "trace:info:";

	/* 运输计划和订单的映射关系 */
	public static final String ORDER_TRACE_RELATION = "trace:order:";


}
