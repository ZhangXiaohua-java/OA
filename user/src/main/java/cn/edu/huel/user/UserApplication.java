package cn.edu.huel.user;

import cn.edu.huel.user.ext.script.DataScript;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


@EnableScheduling
@Slf4j
@EnableFeignClients(basePackages = {"cn.edu.huel.user.feign"})
@EnableRabbit
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
@EnableGlobalAuthentication
@EnableConfigurationProperties
@MapperScan(basePackages = {"cn.edu.huel.user.mapper"})
@SpringBootApplication(scanBasePackages = {"cn.edu.huel"})
public class UserApplication implements CommandLineRunner {

	@Resource
	private DataScript dataScript;

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//dataScript.demo();
		log.info("there is nothing");
	}


}
