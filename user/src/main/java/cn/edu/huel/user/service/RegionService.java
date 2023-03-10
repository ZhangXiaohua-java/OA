package cn.edu.huel.user.service;

import cn.edu.huel.user.domain.Region;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author ZhangXiaoHua
 * @description 针对表【sys_region(地区表)】的数据库操作Service
 * @createDate 2023-03-01 15:03:44
 */
public interface RegionService extends IService<Region> {


	/**
	 * 加载所有的省份信息
	 *
	 * @return
	 */
	List<Region> loadAllProvinces();


	/**
	 * 查找该区域的所有一级子区域
	 *
	 * @param parentRegionId 父region id
	 * @return
	 */
	List<Region> loadAllChildRegions(String parentRegionId);


	/**
	 * @param areaPath 查询城市代码
	 * @return
	 */
	Long queryRegionCode(Long[] areaPath);

	/**
	 * @param regionCode 区县代码
	 * @return 纬度, 精度
	 */
	String queryLngAndLatByRegionCode(String regionCode);


	/**
	 * @param regionCode 区县代码
	 * @return 区县名字
	 */
	String getNameByRegionCode(String regionCode);


	/**
	 * @return 全国所有的地级市信息
	 */
	List<Region> listAllLevel2Cities();

	/***
	 *
	 * @param parentRegionCode 地级市代码
	 * @return 该地级市下所有的区县
	 */
	List<Region> listChildCounts(String parentRegionCode);

	/**
	 * @param code 邮编
	 * @return 根据区域编码查询区域信息
	 */
	Region queryRegionByRegionCode(String code);


	/**
	 * @param code 区县编码
	 * @return 查询上一级行政区信息
	 */
	Region findParentRegionInfo(String code);


	/**
	 * @param zipCode 邮编
	 * @return 区域信息
	 */
	Region queryRegionByZipCode(String zipCode);
	

}

