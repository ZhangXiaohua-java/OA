package com.ruoyi.web.component;

import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * @author 张晓华
 * @date 2023-3-3
 */
@RestControllerAdvice
public class ValidationHandler {


	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	public AjaxResult notValidHandler(MethodArgumentNotValidException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		HashMap<String, String> map = new HashMap<>();
		bindingResult.getFieldErrors().forEach(e -> {
			map.put(e.getField(), e.getDefaultMessage());
		});
		return new AjaxResult(200, "ok").put("data", map);
	}


}
