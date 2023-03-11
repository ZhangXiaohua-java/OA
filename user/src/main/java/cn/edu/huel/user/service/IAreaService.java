package cn.edu.huel.user.service;

import cn.edu.huel.user.domain.Area;
import cn.edu.huel.user.domain.PositionParam;
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


	/**
	 * 根据邮政编码查询改区域的经纬度坐标
	 *
	 * @param zipcode 邮政编码
	 * @return
	 */
	PositionParam queryLngAndLatByZipCode(String zipcode);

	/**
	 * @param id 区域
	 * @return 根据区域id查询该区域的所有子项目
	 */
	List<Area> queryAllChildAreas(Long id);


	/**
	 * @param regionName       区域名
	 * @param parentRegionName 父区域名,用来解决重名的区县问题
	 * @return 经纬度细腻
	 */
	String queryLngAndLatByCountName(String regionName, String parentRegionName);


	/**
	 * @param origin 编码
	 * @return 判断传入的编码是否是邮编, 如果不是则返回其对应的邮编
	 */
	String codeExists(String origin);
}
