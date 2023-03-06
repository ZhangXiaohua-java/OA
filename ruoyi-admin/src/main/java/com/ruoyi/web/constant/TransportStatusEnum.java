package com.ruoyi.web.constant;

/**
 * @author 张晓华
 * @date 2023-3-5
 */
public enum TransportStatusEnum {
	NEW('1', "新建"),
	READY('2', "已经生成了路线"),
	ASSIGN_ROUTE_PATH('3', "已经分配了路线"),
	GONE('4', "进行中"),
	OVER('5', "已结束");
	private char code;

	private String desc;


	TransportStatusEnum(char code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public char getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static TransportStatusEnum getEnumByCode(char code) {
		switch (code) {
			case '1':
				return NEW;
			case '2':
				return READY;
			case '3':
				return ASSIGN_ROUTE_PATH;
			case '4':
				return GONE;
			case '5':
				return OVER;
			default:
				return null;
		}

	}

}
