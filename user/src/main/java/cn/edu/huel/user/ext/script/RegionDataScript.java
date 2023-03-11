package cn.edu.huel.user.ext.script;

import cn.edu.huel.user.base.constant.RedisConstant;
import cn.edu.huel.user.domain.Region;
import cn.edu.huel.user.service.RegionService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

/**
 * @author 张晓华
 * @date 2023-3-7
 */
@Component
public class RegionDataScript {


	@Resource
	private RegionService regionService;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;


	public void task() {
		List<Region> regions = regionService.listAllLevel2Cities();
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		regions.stream().forEach(e -> {
			ops.set(RedisConstant.REGION_INFO_PREFIX + e.getRegionCode(), e, Duration.ofDays(1));
			String parentRegionCode = e.getRegionCode();
			List<Region> children = regionService.listChildCounts(parentRegionCode);
			String key = RedisConstant.REGION_CHILD_INFO_PREFIX + e.getRegionCode();
			BoundListOperations<String, Object> bound = redisTemplate.boundListOps(key);
			bound.rightPushAll(children);
			redisTemplate.expire(key, Duration.ofDays(1));
		});
	}

	public void loadProvinces() {
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		List<Region> regions = regionService.loadAllProvinces();
		for (Region region : regions) {
			String regionName = region.getRegionName();
			ops.set(RedisConstant.REGION_PROVINCE_INFO_PREFIX + regionName, region, Duration.ofDays(1));
		}
	}



}
