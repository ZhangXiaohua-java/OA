package cn.edu.huel.user.base.utils;

import java.util.Objects;

/**
 * @author 张晓华
 * @date 2023-3-9
 */
public final class InfoUtils {


	/**
	 * @param str 敏感信息项目,如身份证号,密码,手机号等
	 * @return 脱敏之后的信息
	 */
	public static String removeSensitiveBit(String str) {
		Objects.requireNonNull(str, "参数不可为空");
		switch (str.length()) {
			case 11:
				return processPhoneNum(str);
			case 18:
				return processIdCard(str);
			default:
				return "";
		}
	}

	private static String processPhoneNum(String phone) {
		return phone.substring(0, 4).concat("****").concat(phone.substring(8));
	}

	private static String processIdCard(String cardNum) {
		return cardNum.substring(0, 12).concat("******");
	}



}
