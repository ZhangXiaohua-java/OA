package cn.edu.huel.user.mapper;

import cn.edu.huel.user.domain.PostOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 订单Mapper接口
 *
 * @author 张晓华
 * @date 2023-02-18
 */
public interface PostOrderMapper extends BaseMapper<PostOrder> {


	/**
	 *
	 * @param customerId 顾客id
	 * @return 未支付金额
	 */
	BigDecimal selectUnpaiedAmount(@Param("customerId") String customerId);


}
