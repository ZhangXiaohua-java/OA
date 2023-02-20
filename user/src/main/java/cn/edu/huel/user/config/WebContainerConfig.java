package cn.edu.huel.user.config;

import cn.edu.huel.user.component.FastJson2RedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;

/**
 * @author 张晓华
 * @date 2023-2-19
 */
@Configuration
public class WebContainerConfig {


	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		RedisSerializer<String> string = RedisSerializer.string();
		FastJson2RedisSerializer<Object> serializer = new FastJson2RedisSerializer(Object.class);
		redisTemplate.setKeySerializer(string);
		redisTemplate.setHashKeySerializer(string);
		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setHashValueSerializer(serializer);
		return redisTemplate;
	}


	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.addAllowedOrigin("http://127.0.0.1:8080");
		corsConfiguration.addAllowedOrigin("http://localhost:8080");
		corsConfiguration.addAllowedOrigin("http://192.168.123.42:8080");
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setMaxAge(Duration.ofMinutes(30));
		UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
		configurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return configurationSource;
	}


}
