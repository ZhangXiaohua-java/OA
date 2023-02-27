package com.ruoyi.web.feign;

import com.ruoyi.api.ServiceToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 张晓华
 * @date 2023-2-27
 */
@Component
public class PerRequestTokenInterceptor implements RequestInterceptor {


	@Resource
	private ServiceToken serviceToken;

	@Override
	public void apply(RequestTemplate requestTemplate) {
		requestTemplate.header("token", serviceToken.token);
	}



}
