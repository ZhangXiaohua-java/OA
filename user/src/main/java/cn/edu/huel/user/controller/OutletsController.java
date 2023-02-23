package cn.edu.huel.user.controller;

import cn.edu.huel.security.vo.Result;
import cn.edu.huel.user.domain.Outlets;
import cn.edu.huel.user.service.OutletsService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author 张晓华
 * @date 2023-2-23
 */
@RestController
@RequestMapping("/outlets")
public class OutletsController {


	@Resource
	private OutletsService outletsService;

	@PreAuthorize("hasAnyAuthority('emp')")
	@GetMapping("/{id}")
	public Result query(@PathVariable Integer id) {
		Outlets outlets = outletsService.getById(id);
		if (Objects.nonNull(outlets)) {
			return Result.ok().put("outlets", outlets);
		} else {
			return Result.error("not data");
		}
	}


}
