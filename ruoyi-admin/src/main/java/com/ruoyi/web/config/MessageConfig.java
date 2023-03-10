package com.ruoyi.web.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 张晓华
 * @date 2023-2-27
 */
@Configuration
public class MessageConfig {


	@Bean
	public Queue dispatchQueue() {
		return QueueBuilder
				.durable("order.dispatch")
				.build();
	}


	@Bean
	public DirectExchange orderExchange() {
		return ExchangeBuilder.directExchange("order.exchange")
				.durable(true)
				.build();
	}

	@Bean
	public Binding dispatchOrderBinding(Queue dispatchQueue, DirectExchange orderExchange) {
		return BindingBuilder.bind(dispatchQueue)
				.to(orderExchange)
				.with("order.dispatch");
	}


	@Bean
	public Queue dispatchOrderQueue() {
		return QueueBuilder.durable("dispatch.order.queue").build();
	}


	@Bean
	public DirectExchange dispatchExchange() {
		return ExchangeBuilder.directExchange("dispatch.order.exchange")
				.durable(true)
				.build();
	}


	@Bean
	public Binding dispatchBinding(Queue dispatchOrderQueue, DirectExchange dispatchExchange) {
		return BindingBuilder.bind(dispatchOrderQueue)
				.to(dispatchExchange).with("dispatch.order");
	}


}
