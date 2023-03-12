package cn.edu.huel.user.service;

import cn.edu.huel.user.domain.BusinessDepartment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 张晓华
 * @date 2023-3-1
 */
public interface IBusinessDepartmentService extends IService<BusinessDepartment> {


	/**
	 * @param countyCode 区县代码
	 * @return 根据区县代码查询营业部信息
	 */
	List<BusinessDepartment> queryBusinessDepartmentsByCountyCode(Long countyCode);


	BusinessDepartment queryBsByUnifiedCode(String unifiedCode);

	/**
	 * @param lng 经度
	 * @param lat 纬度
	 * @return 查询指定经纬度10KM内的最近的营业部信息
	 */
	BusinessDepartment queryBsByLngAndLat(String lng, String lat);
	

}
