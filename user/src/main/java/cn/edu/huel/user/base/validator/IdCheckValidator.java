package cn.edu.huel.user.base.validator;

import cn.hutool.core.util.IdcardUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author 张晓华
 * @date 2023-3-9
 */
public class IdCheckValidator implements ConstraintValidator<IdCheck,String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return IdcardUtil.isValidCard18(value);
	}





}
