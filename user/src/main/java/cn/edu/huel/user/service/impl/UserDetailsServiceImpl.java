package cn.edu.huel.user.service.impl;

import cn.edu.huel.security.domain.SecurityUser;
import cn.edu.huel.security.domain.User;
import cn.edu.huel.user.domain.Customer;
import cn.edu.huel.user.service.ICustomerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.utils.bean.BeanUtils;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author 张晓华
 * @date 2023-2-19
 */
@Service

public class UserDetailsServiceImpl implements UserDetailsService {


	@Resource
	private ICustomerService customerService;


	@Override
	public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
		if (!StringUtils.hasText(phone)) {
			throw new UsernameNotFoundException("用户名为空");
		}
		LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Customer::getPhone, phone);
		Customer customer = customerService.getOne(wrapper);
		if (Objects.isNull(customer)) {
			throw new UsernameNotFoundException("用户: " + phone + "不存在");
		}
		User user = new User();
		BeanUtils.copyProperties(customer, user);
		user.setPermissions(List.of("emp"));
		SecurityUser securityUser = new SecurityUser();
		securityUser.setCurrentUser(user);
		return securityUser;
	}


}
