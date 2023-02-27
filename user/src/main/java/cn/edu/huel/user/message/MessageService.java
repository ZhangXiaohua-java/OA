package cn.edu.huel.user.message;

import cn.edu.huel.user.vo.OrderVO;
import cn.hutool.core.util.IdUtil;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static cn.edu.huel.user.base.constant.MQConstant.ORDER_EXCHANGE;
import static cn.edu.huel.user.base.constant.MQConstant.ORDER_ROUTING_KEY;

/**
 * @author 张晓华
 * @date 2023-2-25
 */
@Component
public class MessageService {

	@Resource
	private RabbitTemplate rabbitTemplate;

	public void sendOrder(OrderVO orderVO) {
		CorrelationData correlationData = new CorrelationData(IdUtil.nanoId());
		rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_ROUTING_KEY, orderVO, correlationData);
	}


}
