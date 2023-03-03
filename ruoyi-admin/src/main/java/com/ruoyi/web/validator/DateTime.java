package com.ruoyi.web.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author 张晓华
 * @date 2023-3-3
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = {DateTimeValidatorImpl.class})
public @interface DateTime {


	String message() default "{javax.validation.constraints.NotNull.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int interval() default 7;


}
