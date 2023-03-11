package com.ruoyi.web.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.Collections;

/**
 * @author 张晓华
 * @date 2023-2-27
 */
@Configuration
public class WebConfig {


	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(connectionFactory);
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		return rabbitTemplate;
	}


	@Bean
	public HttpMessageConverter httpMessageConverter() {
		FastJsonHttpMessageConverter messageConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig config = new FastJsonConfig();
		config.setSerializerFeatures(SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.NotWriteRootClassName, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
		config.setDateFormat("yyyy-MM-dd HH:mm:ss");
		messageConverter.setFastJsonConfig(config);
		messageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
		return messageConverter;
	}

	@Bean
	public MybatisPlusInterceptor pageInterceptor() {
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
		PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
		paginationInnerInterceptor.setDbType(DbType.MYSQL);
		mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
		return mybatisPlusInterceptor;
	}


}
