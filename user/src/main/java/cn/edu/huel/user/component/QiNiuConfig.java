package cn.edu.huel.user.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 张晓华
 * @date 2023-3-9
 */
//@ConfigurationProperties(prefix = "qiniu")
//@Configuration
@Data
// TODO 垃圾
public class QiNiuConfig {

	/* key */
	private String key;

	/* 密钥 */
	private String security;

	/* 存储桶 */
	private String bucket;

	/* 域名前缀 */
	private String prefix;


}
