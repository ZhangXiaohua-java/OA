package cn.edu.huel.user.mapper;

import cn.edu.huel.user.domain.Region;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZhangXiaoHua
 * @description 针对表【sys_region(地区表)】的数据库操作Mapper
 * @createDate 2023-03-01 15:03:44
 * @Entity generator.domain.Region
 */
public interface RegionMapper extends BaseMapper<Region> {


	List<String> selectRegionCodeByAreaId(@Param("areaId") Long areaId);

	/**
	 * @return 全国所有的地级市信息
	 */
	List<Region> selectAllLocalCities();


	/**
	 * @param parentRegionCode 低级市代码
	 * @return 所有区县
	 */
	List<Region> selectChildCountsByParentRegionCode(@Param("parentRegionCode") String parentRegionCode);


	List<Region> selectRegionCodeByZipCode(@Param("zipCode") String zipCode);

	/**
	 * @param collect 父区域id
	 * @return 父区域信息
	 */
	List<Region> queryParentRegionInfo(@Param("collect") List<String> collect);

}





