package cn.edu.huel.user.to;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 张晓华
 * @date 2023-2-28
 */
@AllArgsConstructor
@Data
public class SmsTo {

	/**
	 * 常量信息
	 * ------------------------------------------------------------------------------------------------------------------------
	 */

	/* 请求头中携带的认证信息的key */
	public static final String Authorization = "Authorization";

	/* 认证信息key的前缀 */
	public static final String APP_CODE = "APPCODE ";

	/* 签名id */
	public static final String SMS_SIGN_ID = "2e65b1bb3d054466b82f0c9d125465e2";

	/* 模板id */
	public static final String TEMPLATE_ID = "63698e3463bd490dbc3edc46a20c55f5";

	/**
	 * ------------------------------------------------------------------------------------------------------------------------
	 */



	/* 手机号 */
	private String mobile;

	/* 签名id */
	private String smsSignId;

	/* 短信模板id*/
	private String templateId;

	/* 替换模板,如**code**:12345,**minute**:5这种形式 */
	private String param;

	/* 短信发送成功与否 */
	private boolean result;

	/* 远程服务的响应信息 */
	private String msg;


}
