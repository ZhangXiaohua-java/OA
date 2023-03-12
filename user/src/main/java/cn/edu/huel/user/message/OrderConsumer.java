package cn.edu.huel.user.message;

import cn.edu.huel.user.base.constant.OrderStatusEnum;
import cn.edu.huel.user.base.constant.RedisConstant;
import cn.edu.huel.user.domain.PostOrder;
import cn.edu.huel.user.service.IAreaService;
import cn.edu.huel.user.service.IPostOrderService;
import cn.edu.huel.user.to.OrderDTO;
import cn.edu.huel.user.vo.OrderVO;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

import static cn.edu.huel.user.base.constant.MQConstant.ORDER_QUEQUE;

/**
 * @author 张晓华
 * @date 2023-2-25
 */
@Component
public class OrderConsumer {

	private Snowflake snowflake;

	@Resource
	private IPostOrderService orderService;

	@Resource
	private RabbitTemplate rabbitTemplate;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	private ValueOperations<String, Object> ops;

	@Resource
	private IAreaService areaService;

	@PostConstruct
	public void init() {
		snowflake = IdUtil.getSnowflake(1L);
		ops = redisTemplate.opsForValue();
	}


	@RabbitListener(queues = {ORDER_QUEQUE})
	public void consume(Message message) {
		byte[] bytes = message.getBody();
		Object object = JSON.parseObject(bytes, OrderVO.class);
		if (object instanceof OrderVO orderVO) {
			PostOrder postOrder = new PostOrder();
			String orderId = snowflake.nextIdStr();
			postOrder.setId(orderId);
			orderVO.setOrderId(orderId);
			postOrder.setCustomerId(orderVO.getCustomerId());
			postOrder.setOrderTime(new Date());
			postOrder.setStatus(OrderStatusEnum.NEW.getCode());
			postOrder.setReceiverName(orderVO.getReceiverName());
			postOrder.setReceiverPhone(orderVO.getReceiverPhone());
			postOrder.setOrigin(orderVO.getPosterZipCode() + "," + orderVO.getDetailAddress());
			postOrder.setDest(orderVO.getReceiveZipCode() + "," + orderVO.getDispatchDetailAddress());
			postOrder.setPoiCode(orderVO.getPoiCode());
			// TODO 字典表
			postOrder.setGoodsType(Long.valueOf(orderVO.getGoodsName()));
			orderService.createOrder(postOrder);
			// TODO MQ给系统交换机发送消息请求分配订单,只需要发送一个订单号和详细的地理位置信息即可
			OrderDTO dto = new OrderDTO();
			dto.setOrderID(orderId);
			dto.setCountCode(orderVO.getCountyCode());
			dto.setUnifiedCode(orderVO.getUnifiedCode());
			dto.setDetailAddress(orderVO.getDetailAddress());
			dto.setPhone(orderVO.getPosterPhone());
			CorrelationData correlationData = new CorrelationData(IdUtil.nanoId());
			rabbitTemplate.convertAndSend("order.exchange", "order.dispatch", dto, correlationData);
			// TODO 将订单信息存储到Redis中,24小时候自动删除
			ops.set(RedisConstant.ORDER_PREFIX + orderId, orderVO, Duration.ofDays(1));
		}


	}


}
