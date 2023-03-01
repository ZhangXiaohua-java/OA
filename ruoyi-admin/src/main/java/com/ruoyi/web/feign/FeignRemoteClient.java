package com.ruoyi.web.feign;

import cn.edu.huel.user.domain.Outlets;
import com.ruoyi.web.to.LoginInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 张晓华
 * @date 2023-2-27
 */
@FeignClient(url = "http://localhost:8000", name = "token")
public interface FeignRemoteClient {


	@PostMapping(value = "/user/login")
	String login(@RequestBody LoginInfo loginInfo);


	@GetMapping("/outlets/query/{zipcode}")
	List<Outlets> queryOutletsByZipcode(@PathVariable("zipcode") String zipcode);


	@PostMapping("/order/up/status/{code}")
	String batchUpdateOrderStatus(@RequestBody String[] ids, @PathVariable("code") char code);


	/**
	 * @param orderId  订单号
	 * @param code     状态值
	 * @param employee 员工信息,工号.姓名的格式
	 * @return 操作结果
	 */
	@PostMapping("/order/confirm/collect")
	String confirmCollectOrder(@RequestParam("orderId") String orderId, @RequestParam("code") char code, @RequestParam("employee") String employee);


	@PostMapping("/sms/order/notify")
	String sendSms(@RequestParam("mobile") String mobile, @RequestParam("param") String param);


}
