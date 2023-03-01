package cn.edu.huel.user;

import cn.edu.huel.user.domain.Area;
import cn.edu.huel.user.service.IAreaService;
import cn.edu.huel.user.service.SmsService;
import cn.hutool.Hutool;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.script.ScriptUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserApplicationTests {

	@Resource
	private RabbitTemplate rabbitTemplate;

	@Resource
	private AmqpTemplate amqpTemplate;


	@Resource
	private AmqpAdmin amqpAdmin;


	@Test
	public void contextLoads() {
	}

	@Test
	public void password() {
		String hex = MD5.create().digestHex("xxxxajkadnkladnalkd");
		System.out.println(hex);
		System.out.println(new BCryptPasswordEncoder().encode(hex));
	}

	@Test
	public void id() {
		System.out.println(IdUtil.getSnowflake().nextId());
	}

	@Test
	public void parseToken() {
		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IjkiLCJwZXJtaXNzaW9ucyI6WyJlbXAiXSwiaWQiOiIxNjI3MzU0MTE4MTc4Mjk1ODA4IiwidXNlcm5hbWUiOiLlvKDmmZPljY4ifQ.K8l07QaRKZlO4X_1eGzxoMuTgllKiEygPy8bi_0SAVk";
		boolean verify = JWTUtil.verify(token, "123456".getBytes());
		System.out.println(verify);
	}

	@Test
	public void send() {
		rabbitTemplate.convertAndSend("msg", "hello");
	}

	@Test
	public void createExchange() {
		DirectExchange exchange = ExchangeBuilder.directExchange("dd")
				.durable(true)
				.build();
		amqpAdmin.declareExchange(exchange);
		Queue queue = QueueBuilder.durable("msg").build();
		amqpAdmin.declareQueue(queue);
		Binding binding = BindingBuilder.bind(queue).to(exchange).with("msg");
		amqpAdmin.declareBinding(binding);

	}


	@Resource
	public IAreaService areaService;


	@Test
	public void areasTest() {
		List<Area> areas = areaService.queryAreasByParentId(37L);
		System.out.println(areas);
	}

	@Resource
	private SmsService smsService;

	@Test
	public void sendSms() {
		//boolean res = smsService.sendSms("19937656750", "999999");
		//System.out.println(res ? "ok" : "fail");
	}

	@Test
	public void callLua() throws ScriptException {
		ScriptEngine luaEngine = ScriptUtil.getLuaEngine();
		File file = FileUtil.file("script/VerifyToken.lua");
		String script = FileUtil.readString(file, StandardCharsets.UTF_8);
		luaEngine.eval(script);
	}

	@Test
	public void redisLua() throws IOException {
		ClassPathResource resource = new ClassPathResource("/script/VerifyToken.lua");
		System.out.println(resource.getFile().getAbsolutePath());
		RedisScript<Boolean> script = RedisScript.of(resource, Boolean.class);
	}




}
