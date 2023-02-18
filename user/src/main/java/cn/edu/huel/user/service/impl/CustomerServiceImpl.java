package cn.edu.huel.user.service.impl;

import cn.edu.huel.user.domain.Customer;
import cn.edu.huel.user.mapper.CustomerMapper;
import cn.edu.huel.user.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 顾客个人信息Service业务层处理
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {


}
