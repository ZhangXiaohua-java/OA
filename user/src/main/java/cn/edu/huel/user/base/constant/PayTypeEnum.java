package cn.edu.huel.user.base.constant;

/**
 * @author 张晓华
 * @date 2023-2-23
 */
public enum PayTypeEnum {
	ALI_PAY(1, "支付宝"),
	WECHAT_PAY(2, "微信支付"),
	UNION_PAY(3, "银联支付"),
	FACE_PAY(4, "当面支付"),
	FUTURE_PAY(5, "到付");

	private final int code;

	private final String value;

	PayTypeEnum(int code, String value) {
		this.code = code;
		this.value = value;
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}
}
