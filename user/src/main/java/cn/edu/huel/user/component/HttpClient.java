package cn.edu.huel.user.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 张晓华
 * @date 2023-3-1
 */
@Slf4j
public class HttpClient {

	public static CloseableHttpClient getHttpClient() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(2000);
		connectionManager.setDefaultMaxPerRoute(2000);
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(50000)
				.setSocketTimeout(50000)
				.setConnectionRequestTimeout(50000).build();
		HttpRequestRetryHandler requestRetryHandler = new StandardHttpRequestRetryHandler();
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig)
				.setRetryHandler(requestRetryHandler)
				.build();
		ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
		scheduledThreadPoolExecutor.scheduleWithFixedDelay(() -> {
			connectionManager.closeExpiredConnections();
			connectionManager.closeIdleConnections(60, TimeUnit.SECONDS);
			log.info("关闭空闲连接");
		}, 60, 60, TimeUnit.SECONDS);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				if (httpClient != null) {
					httpClient.close();
					log.info("销毁了httpClient对象");
				}
			} catch (Exception e) {
				log.error("销毁httpClient对象失败");
				e.printStackTrace();
			}
		}));
		return httpClient;
	}



}
