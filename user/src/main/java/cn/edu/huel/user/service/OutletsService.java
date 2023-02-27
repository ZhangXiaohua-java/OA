package cn.edu.huel.user.service;


import cn.edu.huel.user.domain.Outlets;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author ZhangXiaoHua
 * @description 针对表【t_outlets(网点信息表)】的数据库操作Service
 * @createDate 2023-02-23 22:38:04
 */
public interface OutletsService extends IService<Outlets> {

	/**
	 * @param zipcode 邮编
	 * @return 根据邮编查询出所有该区域内的营业网点
	 */
	List<Outlets> queryOutletsByPostCode(String zipcode);



}
