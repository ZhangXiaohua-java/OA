package cn.edu.huel.user;

import cn.edu.huel.user.config.AliOssConfig;
import cn.edu.huel.user.domain.Area;
import cn.edu.huel.user.domain.ServiceSupportRange;
import cn.edu.huel.user.service.IAreaService;
import cn.edu.huel.user.service.IPostOrderService;
import cn.edu.huel.user.service.SmsService;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.script.ScriptUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@SpringBootTest
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


	@Test
	public void parseTest() {
		String content = "{\n" +
				"    \"status\": \"success\",\n" +
				"    \"message\": null,\n" +
				"    \"code\": null,\n" +
				"    \"result\": {\n" +
				"        \"deliveryList\": [\n" +
				"            {\n" +
				"                \"rangeTypeCode\": \"DELIVERY_NATURE_FQJPS\",\n" +
				"                \"rangeTypeName\": \"转寄区域\",\n" +
				"                \"rangeTypeDesc\": null\n" +
				"            },\n" +
				"            {\n" +
				"                \"rangeTypeCode\": \"DELIVERY_NATURE_QJPS\",\n" +
				"                \"rangeTypeName\": \"全境派送\",\n" +
				"                \"rangeTypeDesc\": [\n" +
				"                    {\n" +
				"                        \"streetName\": \"畜牧良种农场\",\n" +
				"                        \"streetCode\": \"410225022\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": \"\"\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"三义寨乡\",\n" +
				"                        \"streetCode\": \"410225007\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": null\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"东坝头镇\",\n" +
				"                        \"streetCode\": \"410225008\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": \"雷集村委会\"\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"小宋镇\",\n" +
				"                        \"streetCode\": \"410225011\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": \"\"\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"桐乡街道\",\n" +
				"                        \"streetCode\": \"410225024\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": \"\"\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"谷营镇\",\n" +
				"                        \"streetCode\": \"410225010\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": null\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"南彰镇\",\n" +
				"                        \"streetCode\": \"410225003\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": \"南彰村委会\"\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"孟寨乡\",\n" +
				"                        \"streetCode\": \"410225012\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": null\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"城关乡\",\n" +
				"                        \"streetCode\": \"410225006\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": null\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"惠安街道\",\n" +
				"                        \"streetCode\": \"410225025\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": \"\"\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"固阳农科所\",\n" +
				"                        \"streetCode\": \"410225023\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": null\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"造纸林场\",\n" +
				"                        \"streetCode\": \"410225019\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": null\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"西关农场\",\n" +
				"                        \"streetCode\": \"410225021\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": \"\"\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"爪营乡\",\n" +
				"                        \"streetCode\": \"410225009\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": null\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"堌阳镇\",\n" +
				"                        \"streetCode\": \"410225002\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": null\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"仪封园艺场\",\n" +
				"                        \"streetCode\": \"410225018\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": \"虚拟生活区\"\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"柳林林场\",\n" +
				"                        \"streetCode\": \"410225020\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": null\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"中原石油勘探三公司\",\n" +
				"                        \"streetCode\": \"410225017\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": \"虚拟社区\"\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"仪封乡\",\n" +
				"                        \"streetCode\": \"410225016\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": \"仪封村委会\"\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"葡萄架乡\",\n" +
				"                        \"streetCode\": \"410225014\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": \"葡萄架村委会\"\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"红庙镇\",\n" +
				"                        \"streetCode\": \"410225005\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": null\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"兰阳街道\",\n" +
				"                        \"streetCode\": \"410225001\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": \"全境派送\"\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"阎楼乡\",\n" +
				"                        \"streetCode\": \"410225015\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": null\n" +
				"                    }\n" +
				"                ]\n" +
				"            },\n" +
				"            {\n" +
				"                \"rangeTypeCode\": \"DELIVERY_NATURE_ZTBPS\",\n" +
				"                \"rangeTypeName\": \"不服务区域\",\n" +
				"                \"rangeTypeDesc\": null\n" +
				"            },\n" +
				"            {\n" +
				"                \"rangeTypeCode\": \"DELIVERY_NATURE_GBQYBS\",\n" +
				"                \"rangeTypeName\": \"全境派送（除特殊区域）\",\n" +
				"                \"rangeTypeDesc\": null\n" +
				"            },\n" +
				"            {\n" +
				"                \"rangeTypeCode\": \"DELIVERY_NATURE_CZTBPS\",\n" +
				"                \"rangeTypeName\": \"乡镇自提\",\n" +
				"                \"rangeTypeDesc\": null\n" +
				"            },\n" +
				"            {\n" +
				"                \"rangeTypeCode\": \"DELIVERY_NATURE_ZZXPS\",\n" +
				"                \"rangeTypeName\": \"镇中心派送\",\n" +
				"                \"rangeTypeDesc\": [\n" +
				"                    {\n" +
				"                        \"streetName\": \"许河乡\",\n" +
				"                        \"streetCode\": \"410225013\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": \"许河村\\n\"\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"streetName\": \"张君墓镇\",\n" +
				"                        \"streetCode\": \"410225004\",\n" +
				"                        \"intoWareHouse\": \"N\",\n" +
				"                        \"townName\": \"张北村 \\n\"\n" +
				"                    }\n" +
				"                ]\n" +
				"            }\n" +
				"        ]\n" +
				"    }\n" +
				"}";
		String level1 = JSON.parseObject(content)
				.get("result")
				.toString();
		String level2 = JSON.parseObject(level1)
				.get("deliveryList").toString();
		JSONArray array = JSON.parseArray(level2);
		for (Object o : array) {
			String s = o.toString();
			Object desc = JSON.parseObject(s).get("rangeTypeDesc");
			if (Objects.nonNull(desc)) {
				System.out.println("----------");
				String res = desc.toString();
				List<ServiceSupportRange> supportRanges = JSON.parseObject(res, new TypeReference<List<ServiceSupportRange>>() {
				});
				System.out.println(supportRanges);
			}
		}
	}


	@Test
	public void read() {
		byte[] bytes = new byte[]{
				97, 112, 112, 108, 105, 99, 97, 116, 105, 111, 110, 47, 106, 115, 111, 110, 59, 32, 99, 104, 97, 114, 115, 101, 116, 61, 85, 84, 70, 45, 56
		};
		System.out.println(new String(bytes, StandardCharsets.UTF_8));
	}


	@Resource
	private IPostOrderService orderService;

	@Resource
	private OSS oss;

	@Resource
	private AliOssConfig config;

	@Test
	public void uploadFile() {
		String filename = "oa/test.mp4";
		PutObjectRequest request = new PutObjectRequest(config.getBucket(),filename,new File("D:\\data\\video\\obs\\2022-06-17_13-47-44.mp4"));
		request.setProcess("true");
		PutObjectResult result = oss.putObject(request);
		ResponseMessage response = result.getResponse();
		int statusCode = response.getStatusCode();
		String uri = response.getUri();
		System.out.println(statusCode);
		System.out.println(uri);
		String str = JSON.toJSONString(response);
		System.out.println(str);

	}





}
