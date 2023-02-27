package com.ruoyi.web.feign;

import cn.edu.huel.user.domain.Outlets;
import com.ruoyi.web.to.LoginInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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


	@PostMapping("/order/up/status")
	String batchUpdateOrderStatus(@RequestBody String[] ids);


}
