package cn.edu.huel.user.base.constant;

/**
 * @author 张晓华
 * @date 2023-2-27
 */
public enum OrderStatusEnum {

	/* New新建,初始状态 */
	NEW(1, "未支付订单"),
	/* 营业部已经确认 */
	CONFIRMED(2, "营业部已接收订单"),

	CREATED(3, "订单创建成功"),

	PAYED(4, "已完成订单支付"),

	POSTING(5, "运输中"),

	DISPATCHING(6, "派送中"),

	Finished(7, "已送达"),

	OVER(8, "订单已关闭"),

	UNKNOWN(9, "无订单信息");

	private int code;

	private String status;

	OrderStatusEnum(int code, String status) {
		this.code = code;
		this.status = status;
	}


	public int getCode() {
		return code;
	}

	public String getStatus() {
		return status;
	}

	public static OrderStatusEnum getInstanceByCode(int code) {
		switch (code) {
			case 1:
				return NEW;
			case 2:
				return CONFIRMED;
			case 3:
				return CREATED;
			case 4:
				return PAYED;
			case 5:
				return POSTING;
			case 6:
				return DISPATCHING;
			case 7:
				return Finished;
			case 8:
				return OVER;
			case 9:
				return UNKNOWN;
			default:
				return null;
		}
	}

	public static void main(String[] args) {
		OrderStatusEnum statusEnum = OrderStatusEnum.getInstanceByCode(1);
		System.out.println(statusEnum.getStatus());
	}

}
