package cn.edu.huel.user.config;

import cn.edu.huel.user.component.FastJson2RedisSerializer;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import feign.codec.Decoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		corsConfiguration.addAllowedOrigin("http://127.0.0.1");
		corsConfiguration.addAllowedOrigin("http://localhost");
		corsConfiguration.addAllowedOrigin("http://192.168.123.42");
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setMaxAge(Duration.ofMinutes(30));
		UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
		configurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return configurationSource;
	}


	@Bean
	public MybatisPlusInterceptor pageInterceptor() {
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
		PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
		paginationInnerInterceptor.setDbType(DbType.MYSQL);
		mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
		return mybatisPlusInterceptor;
	}


	@Bean
	public HttpMessageConverter httpMessageConverter() {
		FastJsonHttpMessageConverter messageConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig config = new FastJsonConfig();
		config.setSerializerFeatures(SerializerFeature.WriteNullBooleanAsFalse,
				SerializerFeature.WriteNullListAsEmpty,
				SerializerFeature.WriteDateUseDateFormat,
				SerializerFeature.NotWriteRootClassName,
				SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullStringAsEmpty);
		config.setDateFormat("yyyy-MM-dd HH:mm:ss");
		config.setCharset(StandardCharsets.UTF_8);
		messageConverter.setFastJsonConfig(config);
		messageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
		return messageConverter;
	}

	public static class ExtMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
		/*解决no suitable HttpMessageConverter found for response type [class com.XXX] and content type [text/plain]*/
		public ExtMappingJackson2HttpMessageConverter() {
			List<MediaType> mediaTypes = new ArrayList<>();
			mediaTypes.add(MediaType.TEXT_PLAIN);
			setSupportedMediaTypes(mediaTypes);// tag6
		}
	}


	// Feign的响应编码配置,无效,先不处理了
	@Bean
	public ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
		final HttpMessageConverters httpMessageConverters = new HttpMessageConverters(new ExtMappingJackson2HttpMessageConverter());
		return new ObjectFactory<HttpMessageConverters>() {
			@Override
			public HttpMessageConverters getObject() throws BeansException {
				return httpMessageConverters;
			}
		};
	}


	@Bean
	public Decoder feignDecoder(ObjectFactory<HttpMessageConverters> convertersObjectFactory) {
		return new ResponseEntityDecoder(new SpringDecoder(convertersObjectFactory));
	}


	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new ExtMappingJackson2HttpMessageConverter());
		return restTemplate;
	}


}
