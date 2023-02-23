package cn.edu.huel.user.service;

import cn.edu.huel.user.domain.Area;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 全国地理位置信息Service接口
 *
 * @author 张晓华
 * @date 2023-02-18
 */
public interface IAreaService extends IService<Area> {


	/**
	 * 根据关键词模糊检索城市
	 *
	 * @param keyword 关键词
	 * @return 满足条件的所有区县级城市
	 */
	List<Area> fuzzySearchAreas(String keyword);


}
