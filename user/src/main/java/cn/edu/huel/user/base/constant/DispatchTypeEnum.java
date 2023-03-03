package cn.edu.huel.user.base.constant;

/**
 * @author 张晓华
 * @date 2023-3-2
 */
public enum DispatchTypeEnum {

	ALL_SUPPORTED('1', "全境派送"),

	SELF_DELIVERY('2', "乡镇自提"),

	CENTER_DELIVERY('3', "镇中心派送");


	private char code;

	private String desc;

	DispatchTypeEnum(char code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public char getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static DispatchTypeEnum getEnumByCode(char code) {
		switch (code) {
			case '1':
				return ALL_SUPPORTED;
			case '2':
				return SELF_DELIVERY;
			case '3':
				return CENTER_DELIVERY;
			default:
				return null;
		}
	}



}
