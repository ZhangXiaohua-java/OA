package cn.edu.huel.user.controller;


import cn.edu.huel.security.vo.Result;
import com.ruoyi.common.core.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单Controller
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@RestController
@RequestMapping("/system/order")
public class PostOrderController extends BaseController {


	@PostMapping("/order")
	public Result order() {
		return Result.ok();
	}


}
