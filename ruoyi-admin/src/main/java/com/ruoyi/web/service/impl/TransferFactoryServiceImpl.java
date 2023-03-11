package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.domain.TransferFactory;
import com.ruoyi.web.mapper.TransferFactoryMapper;
import com.ruoyi.web.service.TransferFactoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhangXiaoHua
 * @description 针对表【t_transfer_factory(转运场信息表)】的数据库操作Service实现
 * @createDate 2023-03-03 15:37:41
 */
@Service
public class TransferFactoryServiceImpl extends ServiceImpl<TransferFactoryMapper, TransferFactory>
		implements TransferFactoryService {


	/**
	 * @return 查询所有二级转原厂
	 */
	@Override
	public List<TransferFactory> queryAllLevel2Factories() {
		LambdaQueryWrapper<TransferFactory> query = new LambdaQueryWrapper<>();
		query.eq(TransferFactory::getLevel, "2");
		return this.baseMapper.selectList(query);
	}


	/**
	 * @param capitalName 转运场名字
	 * @return 模糊查询
	 */
	@Override
	public TransferFactory fuzzyQueryFactory(String capitalName) {
		LambdaQueryWrapper<TransferFactory> query = new LambdaQueryWrapper<>();
		query.like(TransferFactory::getName, "%" + capitalName + "%");
		return this.baseMapper.selectOne(query);
	}

	/**
	 * @param regionCode 城市编码
	 * @return 根据城市编码查询转运场信息
	 */
	@Override
	public TransferFactory queryByRegionCode(String regionCode) {
		LambdaQueryWrapper<TransferFactory> query = new LambdaQueryWrapper<>();
		query.eq(TransferFactory::getRegionCode, regionCode);
		return this.baseMapper.selectOne(query);
	}


}




