package com.ruoyi.web.config;

import com.ruoyi.web.component.CustomThreadFactory;
import com.ruoyi.web.component.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 张晓华
 * @date 2023-3-4
 */
@Configuration
public class ExtensionConfig {

	@Bean
	public Executor executor(CustomThreadFactory threadFactory) {
		return new ThreadPoolExecutor(50,
				100,
				60,
				TimeUnit.SECONDS,
				new ArrayBlockingQueue<>(100),
				threadFactory,
				new ThreadPoolExecutor.AbortPolicy()
		);
	}

	@Bean
	public CloseableHttpClient httpClient() {
		return HttpClient.getHttpClient();
	}




}
