package cn.edu.huel.user.config;

import cn.edu.huel.security.filter.JwtTokenAuthFilter;
import cn.edu.huel.security.filter.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * @author 张晓华
 * @date 2023-2-19
 */
@Configuration
public class SecurityConfig {


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
												   LogoutHandler logoutHandler,
												   LoginFilter loginFilter,
												   UserDetailsService userDetailsService,
												   AccessDeniedHandler accessDeniedHandler,
												   CorsConfigurationSource corsConfigurationSource,
												   JwtTokenAuthFilter jwtTokenAuthFilter) throws Exception {

		return httpSecurity.csrf()
				.disable()
				.authorizeHttpRequests()
				.requestMatchers("/user/login", "/token/check", "/ali/**", "/position/**", "/order/cost/query/**")
				.permitAll()
				.and()
				.authorizeHttpRequests()
				.anyRequest()
				.authenticated()
				.and()
				.logout()
				.logoutUrl("/user/logout")
				.addLogoutHandler(logoutHandler)
				.and().
				addFilter(loginFilter)
				.addFilterBefore(jwtTokenAuthFilter, BasicAuthenticationFilter.class)
				.exceptionHandling()
				.accessDeniedHandler(accessDeniedHandler)
				.and()
				.userDetailsService(userDetailsService)
				.cors()
				.configurationSource(corsConfigurationSource)
				.and()
				.build();
	}


}
