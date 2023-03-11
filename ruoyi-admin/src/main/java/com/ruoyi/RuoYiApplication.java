package com.ruoyi;

import com.ruoyi.web.schedule.TransferDataScript;
import com.ruoyi.web.schedule.TransportPlanRouteTask;
import com.ruoyi.web.service.TransferFactoryService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@Slf4j
@MapperScan(basePackages = {"com.ruoyi.web.mapper"})
@EnableScheduling
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@EnableFeignClients(basePackages = {"com.ruoyi.web.feign"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class RuoYiApplication implements CommandLineRunner {

	@Resource
	private TransferFactoryService transferFactoryService;

	private TransportPlanRouteTask transportPlanRouteTask;

	@Resource
	private Executor executor;

	@Resource
	private TransferDataScript transferDataScript;


	public static void main(String[] args) {
		// System.setProperty("spring.devtools.restart.enabled", "false");
		SpringApplication.run(RuoYiApplication.class, args);
		System.out.println("(♥◠‿◠)ﾉﾞ  若依启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
				" .-------.       ____     __        \n" +
				" |  _ _   \\      \\   \\   /  /    \n" +
				" | ( ' )  |       \\  _. /  '       \n" +
				" |(_ o _) /        _( )_ .'         \n" +
				" | (_,_).' __  ___(_ o _)'          \n" +
				" |  |\\ \\  |  ||   |(_,_)'         \n" +
				" |  | \\ `'   /|   `-'  /           \n" +
				" |  |  \\    /  \\      /           \n" +
				" ''-'   `'-'    `-..-'              ");
	}


	@Override
	public void run(String... args) throws Exception {
		// TODO 异步线程池
		//transferDataScript.setParentId();
		log.info("开始执行运输计划的任务");
	}


}
