package cn.edu.huel.user.base.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 张晓华
 * @date 2023-2-25
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE})
@Constraint(validatedBy = {PayTypeValidator.class})
public @interface PayTypeCheck {

	String range() default "1,2,3,4,5";

	String message() default "请选择有效的支付方式";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};


}
