package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.TransferFactory;

import java.util.List;

/**
 * @author ZhangXiaoHua
 * @description 针对表【t_transfer_factory(转运场信息表)】的数据库操作Service
 * @createDate 2023-03-03 15:37:41
 */
public interface TransferFactoryService extends IService<TransferFactory> {

	/**
	 * @return 查询所有二级转原厂
	 */
	List<TransferFactory> queryAllLevel2Factories();

	/**
	 * @param capitalName 转运场名字
	 * @return 模糊查询
	 */
	TransferFactory fuzzyQueryFactory(String capitalName);


	/**
	 * @param regionCode 城市编码
	 * @return 根据城市编码查询转运场信息
	 */
	TransferFactory queryByRegionCode(String regionCode);


}
