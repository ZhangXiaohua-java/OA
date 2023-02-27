package cn.edu.huel.user.base.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张晓华
 * @date 2023-2-25
 */
public class PayTypeValidator implements ConstraintValidator<PayTypeCheck, Integer> {

	private List<Integer> ranges = null;


	@Override
	public void initialize(PayTypeCheck constraintAnnotation) {
		String range = constraintAnnotation.range();
		if (StringUtils.hasText(range)) {
			String[] strings = range.split(",");
			ranges = new ArrayList<>();
			for (String s : strings) {
				ranges.add(Integer.parseInt(s));
			}
		}
	}


	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		return ranges.contains(value);
	}



}
