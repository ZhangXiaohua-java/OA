package cn.edu.huel.user.controller;

import cn.edu.huel.user.domain.Area;
import cn.edu.huel.user.service.IAreaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 全国地理位置信息Controller
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@RestController
public class AreaController extends BaseController {
	@Autowired
	private IAreaService areaService;

	/**
	 * 查询全国地理位置信息列表
	 */
	@GetMapping("/list")
	public List<Area> list() {
		LambdaQueryWrapper<Area> wrapper = new LambdaQueryWrapper<>();
		wrapper.between(Area::getId, 0, 100);
		return areaService.list(wrapper);
	}


}
