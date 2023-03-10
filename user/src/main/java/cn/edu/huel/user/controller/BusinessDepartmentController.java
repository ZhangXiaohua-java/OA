package cn.edu.huel.user.controller;

import cn.edu.huel.security.vo.Result;
import cn.edu.huel.user.domain.BusinessDepartment;
import cn.edu.huel.user.service.IAreaService;
import cn.edu.huel.user.service.IBusinessDepartmentService;
import cn.edu.huel.user.service.RegionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 张晓华
 * @date 2023-3-2
 */
@Slf4j
@RestController
@RequestMapping("/bs")
public class BusinessDepartmentController {

	@Resource
	private IBusinessDepartmentService businessDepartmentService;

	@Resource
	private IAreaService areaService;

	@Resource
	private RegionService regionService;


	@PostMapping("/query")
	public Result queryBusinessDepartmentsByAreaCode(@RequestBody Long[] areaPath) {
		Long countyCode = 0L;
		// TODO 记录一个坑,三张地理位置信息表,竟然查不出一个完整的信息来
		countyCode = regionService.queryRegionCode(areaPath);
		List<BusinessDepartment> businessDepartments = businessDepartmentService.queryBusinessDepartmentsByCountyCode(countyCode);
		return Result.ok().put("data", businessDepartments);
	}


	@GetMapping("/query/{unifiedCode}")
	public Result queryBsInfoByUnifiedCode(@PathVariable String unifiedCode) {
		BusinessDepartment businessDepartment = businessDepartmentService.queryBsByUnifiedCode(unifiedCode);
		return Result.ok().put("data", businessDepartment);
	}


	/**
	 * @param lng 经度
	 * @param lat 纬度
	 * @return 10KM内最近的营业部的信息
	 */
	@GetMapping("/query/bs/position")
	public Result queryBsByLngAndLat(@RequestParam String lng, @RequestParam String lat) {
		BusinessDepartment bs = businessDepartmentService.queryBsByLngAndLat(lng, lat);
		return Result.ok().put("data", bs);
	}


}
