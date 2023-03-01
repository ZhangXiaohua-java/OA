package cn.edu.huel.user.controller;


import cn.edu.huel.user.service.IIntegralService;
import com.ruoyi.common.core.controller.BaseController;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
 * 顾客积分Controller
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@RestController
public class IntegralController extends BaseController {


	@Resource
	private IIntegralService integralService;





}
