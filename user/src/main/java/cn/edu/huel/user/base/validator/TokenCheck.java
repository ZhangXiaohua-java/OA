package cn.edu.huel.user.base.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author 张晓华
 * @date 2023-3-11
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Constraint(validatedBy = {TokenCheckValidator.class})
public @interface TokenCheck {

	String message() default "无效的订单请求";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};


}
