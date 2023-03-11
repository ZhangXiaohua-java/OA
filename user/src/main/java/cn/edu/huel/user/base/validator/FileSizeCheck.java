package cn.edu.huel.user.base.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author 张晓华
 * @date 2023-3-9
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Constraint(validatedBy = {FileSizeCheckValidtaor.class})
public @interface FileSizeCheck {


	long size() default 1024L;

	String message() default "文件大小超出限制";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};


}
