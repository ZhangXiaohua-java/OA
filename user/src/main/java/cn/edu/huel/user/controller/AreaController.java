package cn.edu.huel.user.controller;

import cn.edu.huel.security.vo.Result;
import cn.edu.huel.user.domain.Area;
import cn.edu.huel.user.service.IAreaService;
import cn.edu.huel.user.service.RegionService;
import cn.edu.huel.user.vo.AreaVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.controller.BaseController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 全国地理位置信息Controller
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@RestController
@RequestMapping("/position")
public class AreaController extends BaseController {

	@Resource
	private IAreaService areaService;

	@Resource
	private RegionService regionService;

	/**
	 * 查询全国地理位置信息列表
	 */
	@PreAuthorize("hasAuthority('admin')")
	@GetMapping("/list")
	public List<Area> list() {
		LambdaQueryWrapper<Area> wrapper = new LambdaQueryWrapper<>();
		wrapper.between(Area::getId, 0, 100);
		return areaService.list(wrapper);
	}


	@PostMapping("/search")
	public Result searchAreasByKeyword(String keyword) {
		List<Area> areas = areaService.fuzzySearchAreas(keyword);
		if (!CollectionUtils.isEmpty(areas)) {
			return Result.ok().put("areas", areas);
		} else {
			return Result.error("no data");
		}
	}

	@GetMapping("/root")
	public Result rootAreas() {
		List<Area> areas = areaService.queryRootAreas();
		List<AreaVo> rootAreas = areas.stream()
				.map(e -> {
					AreaVo vo = new AreaVo();
					vo.setLabel(e.getShortname());
					vo.setValue(e.getId());
					vo.setLeaf(false);
					return vo;
				})
				.collect(Collectors.toList());
		return Result.ok().put("data", rootAreas);
	}


	@GetMapping("/tree")
	public Result treeAreas() {
		List<AreaVo> areas = areaService.queryAreas();
		return Result.ok().put("data", areas);
	}

	@GetMapping("/{pid}")
	public Result listChildrenArea(@PathVariable Long pid) {
		List<Area> areas = areaService.queryAreasByParentId(pid);
		if (!areas.isEmpty()) {
			List<AreaVo> vos = areas.stream()
					.map(e -> new AreaVo(e.getId(), e.getShortname()))
					.collect(Collectors.toList());
			return Result.ok().put("data", vos);
		} else {
			return Result.error("no data");
		}
	}

	@GetMapping("/case/{id}")
	public Result lazyLoadAreas(@PathVariable Long id) {
		List<Area> areas = areaService.queryAllChildAreas(id);
		List<AreaVo> list = areas.stream()
				.map(e -> {
					AreaVo vo = new AreaVo();
					vo.setLabel(e.getShortname());
					vo.setValue(e.getId());
					vo.setLeaf(e.getLevel() == 3);
					return vo;
				}).collect(Collectors.toList());
		return Result.ok().put("data", list);
	}


	@PostMapping("/region/query")
	public Result queryRegionCode(@RequestBody Long[] areaPath) {
		Long code = regionService.queryRegionCode(areaPath);
		return Result.ok().put("data", code);
	}




}
