package cn.edu.huel.user.config;

import com.alipay.api.AlipayConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 张晓华
 * @date 2022-12-14
 */
@Data
@ConfigurationProperties(prefix = "alipay")
@Configuration
public class AliPayWebConfig {


	private String gateway;

	private Long app_id;

	private String sign_type;

	private String private_key;

	private String pub_key;


	@Bean
	public AlipayConfig aliPayConfig() {
		AlipayConfig alipayConfig = new AlipayConfig();
		//设置网关地址
		alipayConfig.setServerUrl(gateway);
		//设置应用ID
		alipayConfig.setAppId(app_id + "");
		//设置应用私钥
		alipayConfig.setPrivateKey(private_key);
		//设置请求格式，固定值json
		alipayConfig.setFormat("json");
		//设置字符集
		alipayConfig.setCharset("utf-8");
		//设置签名类型
		alipayConfig.setSignType(sign_type);
		//设置支付宝公钥
		alipayConfig.setAlipayPublicKey(pub_key);
		return alipayConfig;
	}


}
