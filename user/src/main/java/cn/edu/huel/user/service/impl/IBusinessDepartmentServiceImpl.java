package cn.edu.huel.user.service.impl;

import cn.edu.huel.user.domain.BusinessDepartment;
import cn.edu.huel.user.mapper.BusinessDepartmentMapper;
import cn.edu.huel.user.service.IBusinessDepartmentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 张晓华
 * @date 2023-3-1
 */
@Service
public class IBusinessDepartmentServiceImpl extends ServiceImpl<BusinessDepartmentMapper, BusinessDepartment> implements IBusinessDepartmentService {


	/**
	 * @param countyCode 区县代码
	 * @return 根据区县代码查询营业部信息
	 */
	@Override
	public List<BusinessDepartment> queryBusinessDepartmentsByCountyCode(Long countyCode) {
		LambdaQueryWrapper<BusinessDepartment> query = new LambdaQueryWrapper<>();
		query.eq(BusinessDepartment::getCountyCode,countyCode);
		return this.baseMapper.selectList(query);
	}


}
