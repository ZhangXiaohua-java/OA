package cn.edu.huel.user.component;

import cn.edu.huel.security.vo.Result;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 张晓华
 * @date 2023-2-24
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({MethodArgumentNotValidException.class})
	public Result validationExceptionHandler(MethodArgumentNotValidException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		Map<String, Object> map = bindingResult.getFieldErrors()
				.stream()
				.collect(Collectors.toMap(FieldError::getField, ele -> {
					return ele.getDefaultMessage() == null ? "error" : ele.getDefaultMessage();
				}));
		Result result = Result.error("error");
		result.data = map;
		return result;
	}

	@ExceptionHandler
	public Result defaultExceptionHandler(Throwable throwable) {
		return Result.error(throwable.getMessage());
	}

	

}
