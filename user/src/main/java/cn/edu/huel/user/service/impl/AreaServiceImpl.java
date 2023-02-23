package cn.edu.huel.user.service.impl;

import cn.edu.huel.user.domain.Area;
import cn.edu.huel.user.mapper.AreaMapper;
import cn.edu.huel.user.service.IAreaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 全国地理位置信息Service业务层处理
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements IAreaService {


	@Override
	public List<Area> fuzzySearchAreas(String keyword) {
		LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.like(Area::getShortname, keyword)
				.or()
				.like(Area::getMergerName, keyword)
				.or()
				.like(Area::getName, keyword)
				.or()
				.like(Area::getPinyin, keyword);
		return this.baseMapper.selectList(queryWrapper);
	}


}
