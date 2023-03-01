package cn.edu.huel.user.feign;

import cn.edu.huel.user.to.SmsTo;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * @author 张晓华
 * @date 2023-2-28
 */
@Configuration
public class SmsInterceptor implements RequestInterceptor {

	private static final String APP_CODE = "22074c2ee4434c3d8dce3c18b65b8491";

	@Override
	public void apply(RequestTemplate requestTemplate) {
		requestTemplate.header(SmsTo.Authorization, SmsTo.APP_CODE + APP_CODE);
	}


}
