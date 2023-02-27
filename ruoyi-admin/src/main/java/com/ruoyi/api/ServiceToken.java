package com.ruoyi.api;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.web.feign.FeignRemoteClient;
import com.ruoyi.web.to.LoginInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.locks.LockSupport;

/**
 * @author 张晓华
 * @date 2023-2-27
 */
@Slf4j
@Component
public class ServiceToken {

	@Resource
	private FeignRemoteClient feignRemoteClient;

	public volatile String token = "";

	@Scheduled(initialDelay = 0, fixedRate = 60 * 1000 * 60 * 24)
	public void refreshToken() {
		getToken();
	}


	public void getToken() {
		LoginInfo loginInfo = new LoginInfo("13148282713", "b778d52b3c4714f23e60d2d6c70ba409");
		String res = feignRemoteClient.login(loginInfo);
		Integer code = (Integer) JSON.parseObject(res).get("code");
		if (!code.equals(200)) {
			log.error("执行出错了...");
			return;
		}
		System.out.println(code);
		Object data = JSON.parseObject(res).get("data");
		token = (String) JSON.parseObject(data.toString()).get("token");
		System.out.println(token);
	}


	public void test() {
		ServiceToken serviceToken = this;
		Thread thread = Thread.currentThread();
		Runnable runnable = () -> {
			serviceToken.refreshToken();
			System.out.println("解锁");
			LockSupport.unpark(thread);
		};
		new Thread(runnable).start();
		LockSupport.park();
	}


}
