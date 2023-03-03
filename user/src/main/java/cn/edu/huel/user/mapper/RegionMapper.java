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

}




