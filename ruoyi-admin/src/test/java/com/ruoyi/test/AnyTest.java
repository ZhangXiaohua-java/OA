package com.ruoyi.test;

import cn.edu.huel.user.domain.Region;
import cn.edu.huel.user.vo.Result;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.ruoyi.api.ServiceToken;
import com.ruoyi.web.config.QiNiuConfig;
import com.ruoyi.web.constant.RedisConstant;
import com.ruoyi.web.feign.FeignRemoteClient;
import com.ruoyi.web.service.TransferFactoryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * @author 张晓华
 * @date 2023-2-17
 */
@SpringBootTest
public class AnyTest {


	@Test
	public void id() {
		Snowflake snowflake = IdUtil.getSnowflake(1, 1);
		long id = snowflake.nextId();
		System.out.println(id);
		System.out.println((id + "").length());
	}

	@Test
	public void password() {
		String encode = new BCryptPasswordEncoder().encode("123456");
		System.out.println(encode);
		System.out.println(encode.length());
	}

	@Test
	public void nanoid() {
		String nanoId = IdUtil.nanoId();
		System.out.println(nanoId);
		System.out.println(nanoId.length());
	}


	@Resource
	private ServiceToken serviceToken;

	@Test
	public void getToken() {
		serviceToken.test();
	}

	@Resource
	private TransferFactoryService transferFactoryService;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;


	@Test
	public void createTestData() {
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		Region region = (Region) ops.get("region:info:410200");
		String regionCode = region.getRegionCode();
		BoundListOperations<String, Object> bound = redisTemplate.boundListOps(RedisConstant.REGION_CHILD_INFO_PREFIX + regionCode);
		List<Object> list = bound.range(0, -1);
		System.out.println(list);
	}

	@Resource
	private FeignRemoteClient remoteClient;


	@Test
	public void smsTest() {
		Result result = remoteClient.sendSms("19937656750", "123456");
		System.out.println(result);
	}


	@Resource
	private QiNiuConfig config;

	@Test
	public void upload() throws QiniuException {
		System.out.println(config);
		Auth auth = Auth.create(config.getKey(), config.getSecurity());
		System.out.println(auth);
		String token = auth.uploadToken(config.getBucket(), config.getKey());
		System.out.println(token);
		Configuration configuration = new Configuration(com.qiniu.storage.Region.autoRegion());
		UploadManager uploadManager = new UploadManager(configuration);
		Response response = uploadManager.put(new File("D:\\data\\壁纸\\脸红.jpg"), config.getKey(), token);
		StringMap map = response.jsonToMap();
		System.out.println(map);
		Object key = map.get("key");
		System.out.println("key" + key);

	}


	private static String key = "7a4sfT_xGQpoLZLx";

	private static String security = "W9Gc65lK4ZE";

	public static void main(String[] args) throws QiniuException {
		Auth auth = Auth.create(key, security);
		String token = auth.uploadToken("ele-bak");
		Configuration configuration = new Configuration(com.qiniu.storage.Region.autoRegion());
		UploadManager uploadManager = new UploadManager(configuration);
		File file = new File("D:\\data\\video\\obs\\2023-03-08_13-05-58.mp4");
		Response response = uploadManager.put(file, "video/answer.mp4", token);
		Object o = response.jsonToMap().get("key");
		System.out.println(o);
	}


}
