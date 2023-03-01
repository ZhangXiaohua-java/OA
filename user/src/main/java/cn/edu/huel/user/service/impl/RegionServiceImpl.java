package cn.edu.huel.user.service.impl;

import cn.edu.huel.user.domain.Region;
import cn.edu.huel.user.mapper.RegionMapper;
import cn.edu.huel.user.service.RegionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhangXiaoHua
 * @description 针对表【sys_region(地区表)】的数据库操作Service实现
 * @createDate 2023-03-01 15:03:44
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {


	/**
	 * 加载所有的省份信息
	 *
	 * @return
	 */
	@Override
	public List<Region> loadAllProvinces() {
		LambdaQueryWrapper<Region> query = new LambdaQueryWrapper<>();
		query.eq(Region::getRegionLevel, 1);
		return this.baseMapper.selectList(query);
	}


	/**
	 * 查找该区域的所有一级子区域
	 *
	 * @param parentRegionId 父region id
	 * @return
	 */
	@Override
	public List<Region> loadAllChildRegions(String parentRegionId) {
		LambdaQueryWrapper<Region> query = new LambdaQueryWrapper<>();
		query.eq(Region::getRegionParentId, parentRegionId);
		return this.baseMapper.selectList(query);
	}




}




