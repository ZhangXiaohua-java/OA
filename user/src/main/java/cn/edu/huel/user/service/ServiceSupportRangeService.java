package cn.edu.huel.user.service;

import cn.edu.huel.user.domain.ServiceSupportRange;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author ZhangXiaoHua
 * @description 针对表【t_service_support_range(配送范围支持表)】的数据库操作Service
 * @createDate 2023-03-02 19:57:43
 */
public interface ServiceSupportRangeService extends IService<ServiceSupportRange> {


	/**
	 * @param regionCode 区域代码
	 * @return 该区域的配送详情
	 */
	List<ServiceSupportRange> queryDispatchRangeDetail(String regionCode);



}
