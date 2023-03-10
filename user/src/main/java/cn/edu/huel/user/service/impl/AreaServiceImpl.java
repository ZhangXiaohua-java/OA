package cn.edu.huel.user.service.impl;

import cn.edu.huel.user.domain.Area;
import cn.edu.huel.user.domain.PositionParam;
import cn.edu.huel.user.domain.Region;
import cn.edu.huel.user.mapper.AreaMapper;
import cn.edu.huel.user.service.IAreaService;
import cn.edu.huel.user.service.RegionService;
import cn.edu.huel.user.vo.AreaVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 全国地理位置信息Service业务层处理
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements IAreaService {

	@Resource
	private RegionService regionService;


	@Override
	public List<Area> fuzzySearchAreas(String keyword) {
		LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.like(Area::getShortname, keyword).or().like(Area::getMergerName, keyword).or().like(Area::getName, keyword).or().like(Area::getPinyin, keyword);
		return this.baseMapper.selectList(queryWrapper);
	}


	/**
	 * @return 查询所有根节点(省会)信息
	 */
	@Override
	public List<Area> queryRootAreas() {
		LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.select(Area::getId, Area::getShortname, Area::getLevel);
		queryWrapper.eq(Area::getPid, 0);
		return this.baseMapper.selectList(queryWrapper);
	}

	/**
	 * @return 查询树形机构的地域信息
	 */
	@Override
	public List<AreaVo> queryAreas() {
		LambdaQueryWrapper<Area> wrapper = new LambdaQueryWrapper<>();
		wrapper.select(Area::getId, Area::getShortname);
		List<Area> areas = this.baseMapper.selectList(wrapper);
		List<AreaVo> level1 = areas.stream().filter(e -> e.getPid().equals(0L)).map(e -> new AreaVo(e.getId(), e.getShortname())).collect(Collectors.toList());
		return level1;
	}

	public void recursionFindChildren(List<Area> list, AreaVo areaVo) {
		List<AreaVo> children = list.stream().filter(e -> e.getPid().equals(areaVo.getValue())).map(e -> new AreaVo(e.getId(), e.getShortname())).collect(Collectors.toList());
		areaVo.setChildren(children);
		children.forEach(e -> recursionFindChildren(list, e));
	}


	/**
	 * 根据父id查询子树
	 *
	 * @return
	 */
	@Override
	public List<Area> queryAreasByParentId(Long pid) {
		LambdaQueryWrapper<Area> wrapper = new LambdaQueryWrapper<>();
		wrapper.select(Area::getId, Area::getShortname);
		wrapper.eq(Area::getPid, pid);
		return this.baseMapper.selectList(wrapper);
	}


	/**
	 * 根据地区邮编获取地域名称
	 *
	 * @param zipcode 地区邮编
	 * @return 地区名称
	 */
	@Override
	public String getMergerNameByZipCode(String zipcode) {
		LambdaQueryWrapper<Area> query = new LambdaQueryWrapper<>();
		query.eq(Area::getZipCode, zipcode).select(Area::getMergerName);
		return this.baseMapper.selectOne(query).getMergerName();
	}


	/**
	 * 根据邮政编码查询改区域的经纬度坐标
	 *
	 * @param zipcode 邮政编码
	 * @return
	 */
	@Override
	public PositionParam queryLngAndLatByZipCode(String zipcode) {
		LambdaQueryWrapper<Area> query = new LambdaQueryWrapper<>();
		query.select(Area::getLat, Area::getLng);
		query.eq(Area::getZipCode, zipcode);
		Area area = this.baseMapper.selectOne(query);
		return new PositionParam(Double.parseDouble(area.getLng()), Double.parseDouble(area.getLat()));
	}


	/**
	 * @param id 区域
	 * @return 根据区域id查询该区域的所有子项目
	 */
	@Override
	public List<Area> queryAllChildAreas(Long id) {
		LambdaQueryWrapper<Area> query = new LambdaQueryWrapper<>();
		query.select(Area::getId, Area::getShortname, Area::getLevel);
		query.eq(Area::getPid, id);
		return this.baseMapper.selectList(query);
	}

	/**
	 * @param regionName 区域名
	 * @return 经纬度细腻
	 */
	@Override
	public String queryLngAndLatByCountName(String regionName, String parentRegionName) {
		LambdaQueryWrapper<Area> query = new LambdaQueryWrapper<>();
		query.eq(Area::getName, regionName);
		Area area = this.baseMapper.selectCountLngAndLatByName(regionName, parentRegionName);
		return (area.getLng() + "," + area.getLat());
	}


	/**
	 * @param origin 编码
	 * @return 判断传入的编码是否是邮编, 如果不是则返回其对应的邮编
	 */
	@Override
	public String codeExists(String origin) {
		LambdaQueryWrapper<Area> query = new LambdaQueryWrapper<>();
		query.eq(Area::getZipCode, origin);
		Region region = null;
		if (this.baseMapper.exists(query)) {
			return origin;
		} else {
			region = regionService.queryRegionByZipCode(origin);
			if (Objects.isNull(region)) {
				return null;
			} else {
				query = new LambdaQueryWrapper<>();
				query.eq(Area::getName, region.getRegionName());
				// 存在同名的区县信息,如江北区,长安区...
				List<Area> list = this.baseMapper.selectList(query);
				if (Objects.isNull(list)) {
					return null;
				} else if (list.size() == 1) {
					return list.get(0).getZipCode();
				} else {
					// 去重
					Region parent = regionService.findParentRegionInfo(region.getRegionParentId());
					String regionName = parent.getRegionName();
					Area area = this.baseMapper.selectAreaByNames(region.getRegionName(), parent.getRegionName());
					return area.getZipCode();
				}
			}
		}
	}


	/**
	 * @param zipCode 城市邮编
	 * @return 城市编码
	 */
	@Override
	public String queryRegionCodeByZipCode(String zipCode) {
		return regionService.queryRegionByZipCode(zipCode).getRegionCode();
	}


	/**
	 * @param zipCode 邮编
	 * @return 返回父区域的信息
	 */
	@Override
	public Area queryParentAreaInfo(String zipCode) {
		return this.baseMapper.selectParentInfoByZipcode(zipCode);
	}


}
