package cn.edu.huel.user.mapper;


import cn.edu.huel.user.domain.Area;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 全国地理位置信息Mapper接口
 *
 * @author 张晓华
 * @date 2023-02-18
 */
public interface AreaMapper extends BaseMapper<Area> {


	/**
	 *
	 * @param regionName 区县名
	 * @param parentRegionName 父区域名字
	 * @return 经纬度信息
	 */
	Area selectCountLngAndLatByName(@Param("regionName") String regionName, @Param("parentRegionName") String parentRegionName);

}
