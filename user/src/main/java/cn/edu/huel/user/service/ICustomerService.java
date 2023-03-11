package cn.edu.huel.user.service;


import cn.edu.huel.user.domain.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 顾客个人信息Service接口
 *
 * @author 张晓华
 * @date 2023-02-18
 */
public interface ICustomerService extends IService<Customer> {


	/**
	 * @param id id
	 * @return 顾客信息
	 */
	Customer queryCustomerInfoById(String id);


	/**
	 * @param customer 顾客信息
	 * @return 更新认证信息
	 */
	boolean updateAuthInfo(Customer customer);

}
