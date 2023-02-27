package cn.edu.huel.user.config.component;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 张晓华
 * @date 2023-2-25
 */
@Component
public class RabbitMqCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {


	@Override
	public void confirm(CorrelationData correlationData, boolean b, String s) {
		System.out.println(correlationData);
		System.out.println(b);
		System.out.println(s);
	}


	@Override
	public void returnedMessage(ReturnedMessage returnedMessage) {
		System.out.println(returnedMessage);
	}


}
