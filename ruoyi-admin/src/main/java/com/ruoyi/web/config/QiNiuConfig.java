package com.ruoyi.web.config;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author 张晓华
 * @date 2023-3-7
 */
@Data
@Configuration
public class QiNiuConfig {


	private String key = null;

	private String security = null;

	private String bucket = null;


	private String urlPrefix = "http://rpo27uivs.bkt.clouddn.com";


	@PostConstruct
	public void init() {
		File file = new File("D:\\data\\store\\key.json");
		String str = FileUtil.readString(file, StandardCharsets.UTF_8);
		Node node = JSON.parseObject(str, Node.class);
		this.key = node.getAccessKey();
		this.security = node.getSecretKey();
		this.bucket = node.getBucket();
	}

	@Bean
	public UploadManager uploadManager() {
		com.qiniu.storage.Configuration configuration = new com.qiniu.storage.Configuration(Region.huadongZheJiang2());
		UploadManager uploadManager = new UploadManager(configuration);
		return uploadManager;
	}

	@Bean
	public Auth auth() {
		Auth auth = Auth.create(this.key, this.security);
		return auth;
	}


	@Data
	public static class Node {

		private String accessKey;

		private String secretKey;

		private String bucket;

	}


}
