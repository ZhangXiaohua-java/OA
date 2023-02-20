package cn.edu.huel.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
@EnableGlobalAuthentication
@EnableConfigurationProperties
@MapperScan(basePackages = {"cn.edu.huel.user.mapper"})
@SpringBootApplication(scanBasePackages = {"cn.edu.huel"})
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

}
