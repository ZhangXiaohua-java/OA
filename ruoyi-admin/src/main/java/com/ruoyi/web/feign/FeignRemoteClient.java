package com.ruoyi.web.feign;

import cn.edu.huel.user.domain.Outlets;
import cn.edu.huel.user.to.OrderTo;
import cn.edu.huel.user.to.TraceTo;
import cn.edu.huel.user.vo.Result;
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
	String batchUpdateOrderStatus(@RequestBody String[] ids, @PathVariable("code") Integer code);


	/**
	 * @param orderId  订单号
	 * @param code     状态值
	 * @param employee 员工信息,工号.姓名的格式
	 * @return 操作结果
	 */
	@PostMapping("/order/confirm/collect")
	String confirmCollectOrder(@RequestBody OrderTo orderTo);


	@PostMapping("/sms/order/notify")
	Result sendSms(@RequestParam("mobile") String mobile, @RequestParam("param") String param);


	/**
	 * @param areaPath 路径
	 * @return 查询城市编码
	 */
	@PostMapping("/position/region/query")
	String queryRegionCode(@RequestBody Long[] areaPath);


	/**
	 * @param regionCode 区县代码
	 * @return 查询经纬度信息
	 */
	@GetMapping("/position/region/{regionCode}")
	String getCountDetailPosition(@PathVariable("regionCode") String regionCode);


	@PostMapping("/order/association/trace")
	String associationOrderWithRoutePath(@RequestBody List<TraceTo> tos);


	@PostMapping("/order/dispatch/{id}")
	String dispatchOrder(@PathVariable("id") Integer id);


	/**
	 * @param id 订单号
	 * @return 更新订单状态为已结束
	 */
	@PostMapping("/order/confirm/dispatch/{id}")
	Result orderOver(@PathVariable("id") Long id);


	/**
	 * @param id 订单号
	 * @return 下单顾客的手机号
	 */
	@GetMapping("/order/query/phone/{id}")
	Result queryPhoneNum(@PathVariable("id") String id);


}
