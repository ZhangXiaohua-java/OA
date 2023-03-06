package cn.edu.huel.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.huel.user.domain.OrderPayDetail;
import cn.edu.huel.user.service.OrderPayDetailService;
import cn.edu.huel.user.mapper.OrderPayDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author ZhangXiaoHua
* @description 针对表【t_order_pay_detail(支付明细表)】的数据库操作Service实现
* @createDate 2023-03-06 11:22:18
*/
@Service
public class OrderPayDetailServiceImpl extends ServiceImpl<OrderPayDetailMapper, OrderPayDetail>
    implements OrderPayDetailService{

}




