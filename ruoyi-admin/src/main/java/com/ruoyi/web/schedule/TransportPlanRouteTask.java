package com.ruoyi.web.schedule;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.web.constant.RedisConstant;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.feign.FeignRemoteClient;
import com.ruoyi.web.service.TransportPlanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 张晓华
 * @date 2023-3-4
 */
@Slf4j
@Component
public class TransportPlanRouteTask {


	@Resource
	private TransportPlanService transportPlanService;

	// TODO 挖大坑,Feign的httpClient和自定义的httpClient冲突,敲你嘛
	@Resource
	private CloseableHttpClient httpClient;




	@Resource
	private FeignRemoteClient remoteClient;

	private static final String REMOTE_ADDRESS = "https://restapi.amap.com/v5/direction/driving";


	private static final String AMAP_KEY = "b03fe8c955886c7fbafac0adf0ed4104";

	@Resource
	private RedisTemplate<String, Object> redisTemplate;


	@Async("executor")
	@Scheduled(cron = "0 * * * * ?")
	public void generateRoutePathTask() throws IOException {
		log.info("正在生成路径信息");
		List<TransportPlan> plans = transportPlanService.queryRecentTransportPlan();
		if (Objects.nonNull(plans) && plans.size() != 0) {
			Map<String, String> params = new HashMap<>();
			for (TransportPlan plan : plans) {
				log.info("查询到的运输计划信息{}", plans);
				// TODO 查询出发地和目的地的经纬度信息
				String originPos = remoteClient.getCountDetailPosition(plan.getOrigin());
				String destPos = remoteClient.getCountDetailPosition(plan.getDestination());
				params.put("origin", originPos);
				params.put("destination", destPos);
				params.put("strategy", "37");
				params.put("cartype", "0");
				params.put("extensions", "all");
				params.put("show_fields", "cost,cities,polyline");
				params.put("origin_type", "0");
				getRoutePath(params, plan.getId());
			}
		}
	}

	public void getRoutePath(Map<String, String> params, Integer id) throws IOException {
		ArrayList<RoutePath> routePaths = new ArrayList<>();
		HttpPost post = new HttpPost(REMOTE_ADDRESS);
		ArrayList<NameValuePair> valuePairs = new ArrayList<>();
		valuePairs.add(new BasicNameValuePair("key", AMAP_KEY));
		params.forEach((k, v) -> {
			valuePairs.add(new BasicNameValuePair(k, v));
		});
		post.setEntity(new UrlEncodedFormEntity(valuePairs, StandardCharsets.UTF_8));
		CloseableHttpResponse httpResponse = httpClient.execute(post);
		String result = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
		log.info("规划的路径信息{}", result);
		int index = 1;
		RoutePlan routePlan = JSON.parseObject(result, RoutePlan.class);
		if ("1".equals(routePlan.getStatus())) {
			String rawInfo = result;
			redisTemplate.opsForValue().set(RedisConstant.TRANSPORT_PLAN_ROUTE_PATH_RAW_PREFIX + id, rawInfo);
			Route route = routePlan.getRoute();
			List<Paths> paths = route.getPaths();
			// TODO 统计需要经过的所有城市
			// paths是所有的路线的汇总,从中选择一条路线
			for (Paths path : paths) {
				RoutePath routePath = new RoutePath();
				Cost cost = path.getCost();
				routePath.setDistance(Long.valueOf(path.getDistance()));
				routePath.setTolls(new BigDecimal(cost.getTolls()));
				routePath.setTrafficLights(Long.valueOf(cost.getTraffic_lights()));
				routePath.setTollDistance(Long.valueOf(cost.getToll_distance()));
				ArrayList<City> cities = new ArrayList<>();
				List<Steps> steps = path.getSteps();
				for (Steps step : steps) {
					for (Cities city : step.getCities()) {
						City c = new City();
						BeanUtils.copyProperties(city, c);
						ArrayList<County> counties = new ArrayList<>();
						// 遍历区县信息
						for (Districts district : city.getDistricts()) {
							County county = new County();
							BeanUtils.copyProperties(district, county);
							counties.add(county);
						}
						c.setCounties(counties);
						// TODO 去重逻辑,这里一个大坑,如果要判断集合中是否存在这么一个元素,
						// 由于高德的API返回的数据比较分散,就会导致City对象的cities数据不一致,
						// 因此会导致相同名字和代码的城市会被判定成不同的城市,解决办法就是equals和hashcode函数忽略掉counties属性
						// 机智如我...
						if (cities.contains(c)) {
							int poi = cities.indexOf(c);
							City pre = cities.get(poi);
							List<County> countyList = pre.getCounties();
							for (County county : countyList) {
								c.getCounties().add(county);
							}
							c.setCounties(c.getCounties().stream().distinct().collect(Collectors.toList()));
							cities.remove(c);
						}
						cities.add(c);
					}
				}
				routePath.setCities(cities);
				// 保存规划路径信息
				routePaths.add(routePath);
				String str = JSON.toJSONString(routePath);
				File file = new File("D:\\appsdata\\temp\\test\\方案" + (index++) + ".json");
				FileUtil.writeString(str, file, StandardCharsets.UTF_8);
			}
			redisTemplate.opsForValue().set(RedisConstant.TRANSPORT_PLAN_ROUTE_PATH_PREFIX + id, routePaths);
			// TODO 修改计划状态
			transportPlanService.updatePlanStatus(id, '2');
		}


	}

	@Scheduled(cron = "0 * * * * ?")
	public void scanOrders() {

	}


	public static void main(String[] args) {
		File file = new File("D:\\appsdata\\temp\\test\\cities.json");
		String res = FileUtil.readString(file, StandardCharsets.UTF_8);
		List<City> cities = JSON.parseObject(res, new TypeReference<List<City>>() {
		});
		cities = cities.stream().distinct().collect(Collectors.toList());
		System.out.println(cities);
	}

}
