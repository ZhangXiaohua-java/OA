package cn.edu.huel.user.base.utils;

import org.springframework.context.ApplicationContext;

import java.util.Objects;

/**
 * @author 张晓华
 * @date 2023-3-11
 */
public final class ContextUtils {


	private static ApplicationContext applicationContext;


	public static void setApplicationContext(ApplicationContext applicationContext) {
		ContextUtils.applicationContext = applicationContext;
	}


	public static ApplicationContext getApplicationContext() {
		if (Objects.isNull(applicationContext)) {
			throw new IllegalStateException("context is null");
		}
		return applicationContext;
	}


}
