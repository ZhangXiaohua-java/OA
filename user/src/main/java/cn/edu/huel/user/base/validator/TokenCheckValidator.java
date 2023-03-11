package cn.edu.huel.user.base.validator;

import cn.edu.huel.user.base.constant.RedisConstant;
import cn.edu.huel.user.base.utils.ContextUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

/**
 * @author 张晓华
 * @date 2023-3-11
 */
public class TokenCheckValidator implements ConstraintValidator<TokenCheck, String> {


	private ApplicationContext applicationContext;

	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public void initialize(TokenCheck constraintAnnotation) {
		applicationContext = ContextUtils.getApplicationContext();
		redisTemplate = (RedisTemplate<String, Object>) applicationContext.getAutowireCapableBeanFactory().getBean("redisTemplate");
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		RedisScript<Boolean> redisScript = RedisScript.of(new ClassPathResource("script/VerifyToken.lua"), Boolean.class);
		// 验证token
		return Boolean.TRUE.equals(redisTemplate.execute(redisScript, Collections.singletonList(RedisConstant.ORDER_TOKEN + id), value));
	}


}
