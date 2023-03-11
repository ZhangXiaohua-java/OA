package cn.edu.huel.user.controller;

import cn.edu.huel.security.vo.Result;
import cn.edu.huel.user.base.utils.InfoUtils;
import cn.edu.huel.user.domain.Customer;
import cn.edu.huel.user.service.ICustomerService;
import jakarta.annotation.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张晓华
 * @date 2023-3-9
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Resource
	private ICustomerService customerService;


	@GetMapping("/info")
	public Result queryUserinfo() {
		String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Customer customer = customerService.getById(id);
		// 敏感信息脱敏
		String phone = InfoUtils.removeSensitiveBit(customer.getPhone());
		customer.setPhone(phone);
		String idNum = InfoUtils.removeSensitiveBit(customer.getIdNum());
		customer.setIdNum(idNum);
		customer.setPassword(null);
		return Result.ok().put("data", customer);
	}




}
