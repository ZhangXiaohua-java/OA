package cn.edu.huel.user.config;

import cn.edu.huel.user.config.component.RabbitMqCallback;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cn.edu.huel.user.base.constant.MQConstant.*;

/**
 * @author 张晓华
 * @date 2023-2-25
 */
@Configuration
public class RabbitMqConfig {

	@Bean
	public DirectExchange orderExchange() {
		return ExchangeBuilder.directExchange(ORDER_EXCHANGE)
				.durable(true)
				.build();
	}

	@Bean
	public Queue orderQueue() {
		return QueueBuilder.durable(ORDER_QUEQUE)
				.build();
	}

	@Bean
	public Binding orderExchangeQueueBinding(DirectExchange orderExchange, Queue orderQueue) {
		return BindingBuilder.bind(orderQueue)
				.to(orderExchange)
				.with(ORDER_ROUTING_KEY);
	}


	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, RabbitMqCallback callback) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(connectionFactory);
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		rabbitTemplate.setMandatory(true);
		rabbitTemplate.setConfirmCallback(callback);
		rabbitTemplate.setReturnsCallback(callback);
		return rabbitTemplate;
	}

	@Bean
	public Queue dispatchQueue() {
		return QueueBuilder
				.durable("order.dispatch")
				.build();
	}


	@Bean
	public DirectExchange orderDispatchExchange() {
		return ExchangeBuilder.directExchange("order.exchange")
				.durable(true)
				.build();
	}

	@Bean
	public Binding dispatchOrderBinding(Queue dispatchQueue,DirectExchange orderDispatchExchange) {
		return BindingBuilder.bind(dispatchQueue)
				.to(orderDispatchExchange)
				.with("order.dispatch");
	}




}
