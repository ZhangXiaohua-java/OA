package com.ruoyi.web.constant;

/**
 * @author 张晓华
 * @date 2023-3-6
 */
public enum OrderTaskEnum {


	NEW("0", "新建"),
	CONFIRMED("1", "已确认"),
	COLLECTED("2", "已收件");

	private String code;

	private String desc;


	OrderTaskEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}


	public static OrderTaskEnum getEnumByCode(Integer code) {
		switch (code) {
			case 0:
				return NEW;
			case 1:
				return CONFIRMED;
			case 2:
				return COLLECTED;
			default:
				return null;
		}
	}




}
