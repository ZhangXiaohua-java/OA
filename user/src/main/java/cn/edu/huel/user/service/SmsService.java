package cn.edu.huel.user.service;

import cn.edu.huel.user.to.SmsTo;

/**
 * @author 张晓华
 * @date 2023-2-28
 * 短信发送接口
 */
public interface SmsService {

	/**
	 * 发送短信
	 *
	 * @param sms 要发送的短信详情
	 * @return 执行结果
	 */
	SmsTo sendSms(SmsTo sms);

}
