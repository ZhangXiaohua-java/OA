package cn.edu.huel.user.controller;

import cn.edu.huel.security.vo.Result;
import cn.edu.huel.user.domain.ServiceSupportRange;
import cn.edu.huel.user.service.ServiceSupportRangeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 张晓华
 * @date 2023-3-2
 */
@RestController
@RequestMapping("/dispatch")
public class ServiceSupportRangeController {


	@Resource
	private ServiceSupportRangeService serviceSupportRangeService;


	@PostMapping("/query/range")
	public Result queryDispatchDetail(String regionCode) {
		List<ServiceSupportRange> ranges = serviceSupportRangeService.queryDispatchRangeDetail(regionCode);
		return Result.ok().put("data", ranges);
	}




}
