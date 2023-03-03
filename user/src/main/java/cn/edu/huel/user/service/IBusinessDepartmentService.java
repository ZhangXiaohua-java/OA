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



}
