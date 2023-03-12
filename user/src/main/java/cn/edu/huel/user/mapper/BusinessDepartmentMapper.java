package cn.edu.huel.user.mapper;

import cn.edu.huel.user.domain.BusinessDepartment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 张晓华
 * @date 2023-3-1
 */
public interface BusinessDepartmentMapper extends BaseMapper<BusinessDepartment> {


	/**
	 * @param lng 经度
	 * @param lat 纬度
	 * @return 查询指定经纬度10KM内的最近的营业部信息
	 */
	List<BusinessDepartment> selectBsByPosition(@Param("lng") String lng, @Param("lat") String lat);
}
