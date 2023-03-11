package cn.edu.huel.user.base.utils;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;

import java.util.Map;

/**
 * @author 张晓华
 * @date 2023-3-9
 */

public final class Base64Utils {


	/**
	 * @param bytes 二进制数据
	 * @return 编码后的字符串
	 */
	public static String imageToStr(byte[] bytes) {
		return Base64Encoder.encode(bytes);
	}

	/**
	 * @param str Base64字符串,不包含data:image/jpeg;base64,这部分,如果需要自己进行拼接
	 * @return 字节数据
	 */
	public static byte[] strToBytes(String str) {
		return Base64Decoder.decode(str);
	}

	public String concatUrl(String base, Map<String, String> params) {
		return "";
	}


}
