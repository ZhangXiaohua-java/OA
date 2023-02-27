package cn.edu.huel.user.base.constant;

/**
 * @author 张晓华
 * @date 2023-2-27
 */
public enum OrderStatusEnum {

	/* New新建,初始状态 */
	NEW('1', "未支付订单"),
	/* 营业部已经确认 */
	CONFIRMED('2', "营业部已接收订单"),

	CREATED('3', "订单创建成功"),

	PAYED('4', "已完成订单支付"),

	POSTING('5', "运输中"),

	DISPATCHING('6', "派送中"),

	Finished('7', "已送达"),

	OVER('8', "订单已关闭"),

	UNKNOWN('9', "无订单信息");

	private char code;

	private String status;

	OrderStatusEnum(char code, String status) {
		this.code = code;
		this.status = status;
	}


	public char getCode() {
		return code;
	}

	public String getStatus() {
		return status;
	}


}
