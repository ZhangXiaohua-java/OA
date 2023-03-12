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
		query.eq(BusinessDepartment::getCountyCode, countyCode);
		return this.baseMapper.selectList(query);
	}


	@Override
	public BusinessDepartment queryBsByUnifiedCode(String unifiedCode) {
		LambdaQueryWrapper<BusinessDepartment> query = new LambdaQueryWrapper<>();
		query.eq(BusinessDepartment::getUnifiedCode, unifiedCode);
		query.select(BusinessDepartment::getName, BusinessDepartment::getCityName,
				BusinessDepartment::getCountyCode);
		return this.baseMapper.selectOne(query);
	}

	/**
	 * @param lng 经度
	 * @param lat 纬度
	 * @return 查询指定经纬度10KM内的最近的营业部信息
	 */
	@Override
	public BusinessDepartment queryBsByLngAndLat(String lng, String lat) {
		List<BusinessDepartment> businessDepartments = this.baseMapper.selectBsByPosition(lng, lat);
		return businessDepartments.get(0);
	}
	

}
