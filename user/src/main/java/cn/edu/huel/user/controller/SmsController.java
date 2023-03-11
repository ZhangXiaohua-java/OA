package cn.edu.huel.user.controller;

import cn.edu.huel.security.vo.Result;
import cn.edu.huel.user.service.SmsService;
import cn.edu.huel.user.to.SmsTo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张晓华
 * @date 2023-2-28
 */
@RestController
@RequestMapping("/sms")
public class SmsController {

	@Resource
	private SmsService smsService;

	/**
	 * 发送短信
	 *
	 * @param smsTo 参数 mobile 手机号 param 消息内容
	 * @return 发送短信
	 */
	@PostMapping("/order/notify")
	public Result sendCollectGoodsSms(@RequestParam String mobile, @RequestParam String param) {
		SmsTo to = new SmsTo();
		to.setMobile(mobile);
		to.setParam(param);
		SmsTo sms = smsService.sendSms(to);
		if (sms.isResult()) {
			return Result.ok();
		} else {
			return Result.error(sms.getMsg());
		}
	}


}
