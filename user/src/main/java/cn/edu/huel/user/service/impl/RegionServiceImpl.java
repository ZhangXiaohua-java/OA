package cn.edu.huel.user.service.impl;

import cn.edu.huel.user.domain.Area;
import cn.edu.huel.user.domain.Region;
import cn.edu.huel.user.mapper.RegionMapper;
import cn.edu.huel.user.service.IAreaService;
import cn.edu.huel.user.service.RegionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZhangXiaoHua
 * @description 针对表【sys_region(地区表)】的数据库操作Service实现
 * @createDate 2023-03-01 15:03:44
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {


	@Resource
	private IAreaService areaService;

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


	/**
	 * @param areaPath 查询城市代码
	 * @return
	 */
	@Override
	public Long queryRegionCode(Long[] areaPath) {
		List<String> results = this.baseMapper.selectRegionCodeByAreaId(areaPath[2]);
		//List<Long> codes = results.stream().map(Long::valueOf).collect(Collectors.toList());
		// 如果查询出的结果多余一个就需要进行再次的过滤
		if (results.size() > 1) {
			Area parent = areaService.getById(areaPath[1]);
			results = results.stream()
					.filter(e -> {
						LambdaQueryWrapper<Region> query = new LambdaQueryWrapper<>();
						query.eq(Region::getRegionCode, e);
						Region region = this.baseMapper.selectOne(query);
						LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
						wrapper.eq(Region::getRegionId, region.getRegionParentId());
						region = this.baseMapper.selectOne(wrapper);
						return region.getRegionName().equals(parent.getName());
					}).collect(Collectors.toList());
		}
		return Long.valueOf(results.get(0));
	}


	/**
	 * @param regionCode 区县代码
	 * @return 纬度, 精度
	 */
	@Override
	public String queryLngAndLatByRegionCode(String regionCode) {
		LambdaQueryWrapper<Region> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Region::getRegionCode, regionCode)
				.select(Region::getRegionName, Region::getRegionParentId);
		Region region = this.baseMapper.selectOne(queryWrapper);
		String regionName = this.baseMapper.selectOne(new LambdaQueryWrapper<Region>().eq(Region::getRegionId, region.getRegionParentId())).getRegionName();
		return areaService.queryLngAndLatByCountName(region.getRegionName(), regionName);
	}


	/**
	 * @param regionCode 区县代码
	 * @return 区县名字
	 */
	@Override
	public String getNameByRegionCode(String regionCode) {
		LambdaQueryWrapper<Region> query = new LambdaQueryWrapper<>();
		query.eq(Region::getRegionCode, regionCode);
		query.select(Region::getRegionName);
		Region region = this.baseMapper.selectOne(query);
		return region == null ? null : region.getRegionName();
	}


	/**
	 * @return 全国所有的地级市信息
	 */
	@Override
	public List<Region> listAllLevel2Cities() {
		return this.baseMapper.selectAllLocalCities();
	}


	/***
	 *
	 * @param parentRegionCode 地级市代码
	 * @return 该地级市下所有的区县
	 */
	@Override
	public List<Region> listChildCounts(String parentRegionCode) {
		return this.baseMapper.selectChildCountsByParentRegionCode(parentRegionCode);
	}


	/**
	 * @param code 邮编
	 * @return 根据区域编码查询区域信息
	 */
	@Override
	public Region queryRegionByRegionCode(String code) {
		LambdaQueryWrapper<Region> query = new LambdaQueryWrapper<>();
		query.eq(Region::getRegionCode, code);
		return this.baseMapper.selectOne(query);
	}


	/**
	 * @param code 区县编码
	 * @return 查询上一级行政区信息
	 */
	@Override
	public Region findParentRegionInfo(String code) {
		LambdaQueryWrapper<Region> query = new LambdaQueryWrapper<>();
		query.eq(Region::getRegionId, code);
		return this.baseMapper.selectOne(query);
	}


	/**
	 * @param zipCode 邮编
	 * @return 区域信息
	 */
	@Override
	public Region queryRegionByZipCode(String zipCode) {
		List<Region> regions = this.baseMapper.selectRegionCodeByZipCode(zipCode);
		if (regions != null && regions.size() == 1) {
			return regions.get(0);
		} else if (regions != null && regions.size() > 1) {
			Area area = areaService.queryParentAreaInfo(zipCode);
			List<Region> parents = this.baseMapper.queryParentRegionInfo(regions.
					stream().map(e -> e.getRegionParentId()).collect(Collectors.toList()));
			Region parent = parents.stream().filter(e -> e.getRegionName().equals(area.getName())).findAny().get();
			return regions.stream().filter(e -> e.getRegionParentId().equals(parent.getRegionId())).findAny().get();
		}
		return null;
	}
	

}




