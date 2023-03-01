package cn.edu.huel.user.to;

import lombok.Data;

/**
 * @author 张晓华
 * @date 2023-2-28
 */
@Data
public class SmsMessageDto {

	/* 状态码 */
	private Integer code;

	/* 操作结果 */
	private String msg;

	/* 短信id */
	private String smsid;

	/* 可发送短信余量 */
	private Integer balance;


}
