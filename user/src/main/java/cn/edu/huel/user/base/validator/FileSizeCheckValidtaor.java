package cn.edu.huel.user.base.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

/**
 * @author 张晓华
 * @date 2023-3-9
 */
public class FileSizeCheckValidtaor implements ConstraintValidator<FileSizeCheck, MultipartFile> {


	/**
	 * 限制提交的大小为50KB
	 *
	 * @return
	 */
	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		if (Objects.isNull(value)) {
			return false;
		}
		try {
			int available = value.getInputStream().available();
			return available <= 1024 * 50;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}


}
