package cn.edu.huel.user.mapper;

import cn.edu.huel.user.domain.Integral;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 顾客积分Mapper接口
 *
 * @author 张晓华
 * @date 2023-02-18
 */
public interface IntegralMapper extends BaseMapper<Integral> {


	/**
	 * 查询该顾客最近一次的积分记录
	 *
	 * @param customerId 顾客id
	 * @return 执行result
	 */
	Integral queryLastIntegral(@Param("customerId") String customerId);


}
