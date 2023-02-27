package cn.edu.huel.user.service.impl;


import cn.edu.huel.user.domain.Outlets;
import cn.edu.huel.user.mapper.OutletsMapper;
import cn.edu.huel.user.service.OutletsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhangXiaoHua
 * @description 针对表【t_outlets(网点信息表)】的数据库操作Service实现
 * @createDate 2023-02-23 22:38:04
 */
@Service
public class OutletsServiceImpl extends ServiceImpl<OutletsMapper, Outlets>
		implements OutletsService {


	/**
	 * @param zipcode 邮编
	 * @return 根据邮编查询出所有该区域内的营业网点
	 */
	@Override
	public List<Outlets> queryOutletsByPostCode(String zipcode) {
		LambdaQueryWrapper<Outlets> query = new LambdaQueryWrapper<>();
		query.eq(Outlets::getPostCode, zipcode);
		List<Outlets> outlets = this.baseMapper.selectList(query);
		return outlets;
	}


}




