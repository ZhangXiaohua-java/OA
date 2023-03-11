package com.ruoyi.web.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.Protocol;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 张晓华
 * @date 2023-3-9
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "ali.oss.config")
@Configuration
public class AliOssConfig {

	/* key */
	private String key;

	/* 密钥 */
	private String security;

	/* 存储桶 */
	private String bucket;

	/* 域名前缀 */
	private String prefix;


	private String endpoint;


	@Bean
	public OSS oos() {
		ClientBuilderConfiguration configuration = new ClientBuilderConfiguration();
		configuration.setMaxConnections(50);
		configuration.setConnectionTTL(3000);
		configuration.setMaxErrorRetry(6);
		// 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
		configuration.setMaxConnections(200);
		// 设置Socket层传输数据的超时时间，默认为50000毫秒。
		configuration.setSocketTimeout(10000);
		// 设置建立连接的超时时间，默认为50000毫秒。
		configuration.setConnectionTimeout(10000);
		// 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
		configuration.setConnectionRequestTimeout(1000);
		// 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
		configuration.setIdleConnectionTime(10000);
		// 设置失败请求重试次数，默认为3次。
		configuration.setMaxErrorRetry(5);
		configuration.setSLDEnabled(true);
		// 设置连接OSS所使用的协议（HTTP或HTTPS），默认为HTTP。
		configuration.setProtocol(Protocol.HTTP);
		// 设置用户代理，指HTTP的User-Agent头，默认为aliyun-sdk-java。
		configuration.setUserAgent("aliyun-sdk-java");
		OSS oss = new OSSClientBuilder().build(endpoint, key, security);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			log.info("关闭和阿里云的连接");
			oss.shutdown();
		}));
		return oss;
	}


}
