package cn.edu.huel.user.service.impl;

import cn.edu.huel.user.feign.SmsFeignRemoteClient;
import cn.edu.huel.user.service.SmsService;
import cn.edu.huel.user.to.SmsMessageDto;
import cn.edu.huel.user.to.SmsTo;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static cn.edu.huel.user.to.SmsTo.SMS_SIGN_ID;
import static cn.edu.huel.user.to.SmsTo.TEMPLATE_ID;

/**
 * @author 张晓华
 * @date 2023-2-28
 */
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {


	@Resource
	private SmsFeignRemoteClient remoteClient;


	/**
	 * 发送短信
	 *
	 * @param sms 要发送的短信内容
	 * @return 执行结果
	 */
	@Override
	public SmsTo sendSms(SmsTo sms) {
		String result = remoteClient.sendSms(sms.getMobile(), SMS_SIGN_ID, TEMPLATE_ID, "**code**:" + sms.getParam());
		SmsMessageDto dto = JSON.parseObject(result, SmsMessageDto.class);
		log.info("原始的响应信息{}", result);
		log.info("给{}发送了短信,内容:{},远程服务的响应结果:{}", sms.getMobile(), sms.getParam(), dto);
		sms.setResult(dto.getCode().equals(0));
		return sms;
	}


}
