package cn.edu.huel.user.controller;

import cn.edu.huel.security.vo.Result;
import com.ruoyi.common.core.controller.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 运输历史Controller
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@RestController
@RequestMapping("/transport")
public class TransportController extends BaseController {


	@GetMapping("/{orderId}")
	public Result countCost(@PathVariable String orderId) {

		return Result.ok();
	}


}
