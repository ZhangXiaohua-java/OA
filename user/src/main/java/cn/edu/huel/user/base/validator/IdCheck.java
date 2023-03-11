package cn.edu.huel.user.base.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 张晓华
 * @date 2023-3-9
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = {IdCheckValidator.class})
public @interface IdCheck {


	String message() default "无效的身份证号";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
