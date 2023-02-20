package cn.edu.huel.user;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class UserApplicationTests {

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

}
