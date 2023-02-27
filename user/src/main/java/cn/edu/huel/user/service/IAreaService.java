package cn.edu.huel.user.service;

import cn.edu.huel.user.domain.Area;
import cn.edu.huel.user.vo.AreaVo;
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


	/**
	 * @return 查询所有根节点(省会)信息
	 */
	List<Area> queryRootAreas();


	/**
	 * @return 查询树形机构的地域信息
	 */
	List<AreaVo> queryAreas();


	/**
	 * 根据父id查询子树
	 *
	 * @param pid 父id
	 * @return
	 */
	List<Area> queryAreasByParentId(Long pid);


	/**
	 * 根据地区邮编获取地域名称
	 *
	 * @param zipcode 地区邮编
	 * @return 地区名称
	 */
	String getMergerNameByZipCode(String zipcode);


}
