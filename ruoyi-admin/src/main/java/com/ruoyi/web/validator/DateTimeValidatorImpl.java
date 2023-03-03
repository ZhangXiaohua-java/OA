package com.ruoyi.web.validator;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 张晓华
 * @date 2023-3-3
 */
@Component
public class DateTimeValidatorImpl implements ConstraintValidator<DateTime, Date> {


	private int interval = 0;

	@Override
	public void initialize(DateTime constraintAnnotation) {
		interval = constraintAnnotation.interval();
	}

	@Override
	public boolean isValid(Date value, ConstraintValidatorContext context) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, interval);
		Date date = calendar.getTime();
		return value.before(date) && value.after(new Date());
	}


}
