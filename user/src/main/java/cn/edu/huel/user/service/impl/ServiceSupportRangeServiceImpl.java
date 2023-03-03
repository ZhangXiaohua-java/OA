package cn.edu.huel.user.service.impl;

import cn.edu.huel.user.domain.ServiceSupportRange;
import cn.edu.huel.user.mapper.ServiceSupportRangeMapper;
import cn.edu.huel.user.service.ServiceSupportRangeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhangXiaoHua
 * @description 针对表【t_service_support_range(配送范围支持表)】的数据库操作Service实现
 * @createDate 2023-03-02 19:57:43
 */
@Service
public class ServiceSupportRangeServiceImpl extends ServiceImpl<ServiceSupportRangeMapper, ServiceSupportRange>
		implements ServiceSupportRangeService {


	/**
	 * @param regionCode 区域代码
	 * @return 该区域的配送详情
	 */
	@Override
	public List<ServiceSupportRange> queryDispatchRangeDetail(String regionCode) {
		LambdaQueryWrapper<ServiceSupportRange> query = new LambdaQueryWrapper<>();
		query.eq(ServiceSupportRange::getCountCode, regionCode);
		return this.baseMapper.selectList(query);
	}


}




