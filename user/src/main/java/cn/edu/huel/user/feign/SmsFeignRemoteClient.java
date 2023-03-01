package cn.edu.huel.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 张晓华
 * @date 2023-2-28
 */
@FeignClient(url = "https://gyytz.market.alicloudapi.com", name = "sms")
public interface SmsFeignRemoteClient {


	/**
	 * 坑逼,响应json格式的数据,但是响应编码是text/html
	 */
	@PostMapping(value = "/sms/smsSend", produces = {MediaType.APPLICATION_JSON_VALUE})
	String sendSms(@RequestParam("mobile") String mobile,
				   @RequestParam("smsSignId") String smsSignId,
				   @RequestParam("templateId") String templateId,
				   @RequestParam("param") String param);


}
