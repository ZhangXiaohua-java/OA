package com.ruoyi.web.schedule;

import cn.edu.huel.user.to.TraceTo;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.web.constant.RedisConstant;
import com.ruoyi.web.constant.TransportStatusEnum;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.feign.FeignRemoteClient;
import com.ruoyi.web.service.OrderTaskService;
import com.ruoyi.web.service.TransportPlanDetailService;
import com.ruoyi.web.service.TransportPlanService;
import com.ruoyi.web.service.TransportTraceHistoryService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * <<论屎山是怎么形成的>>
 *
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

	@Resource
	private TransportTraceHistoryService transportTraceHistoryService;


	@Resource
	private OrderTaskService orderTaskService;


	@Resource
	private TransportPlanDetailService transportPlanDetailService;

	@Resource
	private RabbitTemplate rabbitTemplate;

	private volatile DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
		// TODO 扫描所有起点和终点都在路径中的订单信息
		List<TransportPlan> transportPlans = transportPlanService.queryRecentHadAssignedRoutePathPlans();
		if (Objects.isNull(transportPlans) || transportPlans.size() == 0) {
			log.info("nothing to do ...");
			return;
		}
		log.info("扫描订单...");
		for (TransportPlan transportPlan : transportPlans) {
			// TODO 重量和体积的处理
			processSinglePlan(transportPlan);
		}

	}


	public void processSinglePlan(TransportPlan plan) {
		TransportPlanDetail planDetail = transportPlanDetailService.getById(plan.getDetailId());
		// TODO 加锁
		RoutePath routePath = JSON.parseObject(new String(plan.getRoutePath(), StandardCharsets.UTF_8), RoutePath.class);
		List<County> counties = routePath.getCities()
				.stream()
				.flatMap(e -> e.getCounties().stream())
				.collect(Collectors.toList());
		// 城市区号集合
		List<String> regionCodes = counties.stream().map(County::getAdcode).collect(Collectors.toList());
		// 订单id集合
		Set<String> ids = new HashSet<>();
		// 保存下订单号和城市区号的对应关系,后面如果所有的订单的重量或体积超出本次运输的运量还要重新存到Redis中去
		Set<Node> nodes = new HashSet<>();
		Set<Node> finalNodes = nodes;
		regionCodes.stream().forEach(e -> {
			String timeStr = dateTimeFormatter.format(LocalDate.now());
			String key = RedisConstant.REGION_ORDER_TRANSPORT_PREFIX + timeStr + ":" + e;
			if (redisTemplate.hasKey(key)) {
				BoundSetOperations<String, Object> ops = redisTemplate.boundSetOps(key);
				Collection<Object> members = ops.members();
				redisTemplate.delete(key);
				if (members != null || members.size() != 0) {
					members.stream().forEach(ele -> {
						ids.add(ele.toString());
						finalNodes.add(new Node(ele.toString(), e));
					});
				}
			}
		});
		if (ids != null && ids.size() != 0) {
			// TODO 处理所有订单的重量和体积
			List<OrderTask> tasks = orderTaskService.listTasksByOrderIds(ids);
			List<Integer> weights = tasks.stream()
					.map(e -> e.getConfirmWeight()).collect(Collectors.toList());
			List<Integer> volumes = tasks.stream()
					.map(e -> e.getConfirmVolume()).collect(Collectors.toList());
			BigDecimal totalWeight = reduce(weights);
			BigDecimal totalVolume = reduce(volumes);
			// 完成单位的转换,立方米到立方厘米,吨到千克
			BigDecimal expectWeight = new BigDecimal(planDetail.getExpectWeight()).multiply(new BigDecimal("1000"));
			BigDecimal expectVolume = new BigDecimal(planDetail.getExpectCapacity()).multiply(new BigDecimal("1000000"));
			if (totalWeight.compareTo(expectWeight) > 0 || totalVolume.compareTo(expectVolume) > 0) {
				// TODO 超出重量或者体积限制,需要做进一步处理
				List<TraceTo> tos = new ArrayList<>();
				BigDecimal weightAccumulator = new BigDecimal("0");
				BigDecimal volumeAccumulator = new BigDecimal("0");
				for (String id : ids) {
					if (weightAccumulator.compareTo(expectWeight) > 0
							|| volumeAccumulator.compareTo(expectVolume) > 0
					) {
						List<String> hasOperatedIds = tos.stream().map(e -> e.getOrderId()).collect(Collectors.toList());
						ids.removeAll(hasOperatedIds);
						nodes = nodes.stream().filter(element -> ids.contains(element.getOrderId()))
								.collect(Collectors.toSet());
						String timeStr = dateTimeFormatter.format(LocalDate.now());
						// 将没有被加入到当前运输计划的订单重新放回Redis中去
						nodes.stream().forEach(e -> {
							String key = RedisConstant.REGION_ORDER_TRANSPORT_PREFIX + timeStr + ":" + e.getRegionCode();
							BoundSetOperations<String, Object> ops = redisTemplate.boundSetOps(key);
							ops.add(e.getOrderId());
						});
						break;
					}
					TraceTo to = new TraceTo();
					to.setOrderId(id);
					to.setTraceId(plan.getId());
					tos.add(to);
					tasks.stream().filter(e -> e.getOrderId().equals(id)).findAny()
							.ifPresent(element -> {
								weightAccumulator.add(new BigDecimal(element.getConfirmWeight()));
								volumeAccumulator.add(new BigDecimal(element.getConfirmVolume()));
							});
				}
				String result = remoteClient.associationOrderWithRoutePath(tos);
				ListOperations<String, Object> ops = redisTemplate.opsForList();
				List<TraceTo> orderIds = tos.stream().collect(Collectors.toList());
				ops.rightPushAll(RedisConstant.ORDER_TRACE_RELATION + plan.getId(), orderIds);
				log.info("修改的结果{}", result);
			} else {
				List<TraceTo> tos = ids.stream().map(e -> {
					TraceTo to = new TraceTo();
					to.setOrderId(e);
					to.setTraceId(plan.getId());
					return to;
				}).collect(Collectors.toList());
				// TODO 建立订单和运输计划之间的关联关系  修改订单状态
				ListOperations<String, Object> ops = redisTemplate.opsForList();
				ops.rightPushAll(RedisConstant.ORDER_TRACE_RELATION + plan.getId(), ids);
				String result = remoteClient.associationOrderWithRoutePath(tos);
				log.info("修改的结果{}", result);
			}
		}

	}


	public BigDecimal reduce(List<Integer> collection) {
		List<BigDecimal> collect = collection.stream().map(e -> new BigDecimal(e)).collect(Collectors.toList());
		return collect.stream()
				.reduce(new BigDecimal("0"), (pre, curr) -> {
					return curr.add(pre);
				});
	}

	@Scheduled(cron = "0 * * * * ?")
	public void transport() {
		List<TransportPlan> plans = transportPlanService.queryAllReadyPlans();
		log.info("查询到的待运输的任务{}", plans);
		for (TransportPlan plan : plans) {
			generateSinglePlanHistory(plan);
			if (plan.getStatus() != TransportStatusEnum.GONE.getCode() || plan.getStatus() != TransportStatusEnum.OVER.getCode()) {
				transportPlanService.updatePlanStatus(plan.getId(), TransportStatusEnum.OVER.getCode());
			}
		}
	}

	public void generateSinglePlanHistory(TransportPlan plan) {
		RoutePath routePath = JSON.parseObject(new String(plan.getRoutePath(), StandardCharsets.UTF_8), RoutePath.class);
		// 每个四个城市停留一次,更新一次记录
		List<City> cities = routePath.getCities();
		ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
		// 取较大值
		int count = Math.max(threadLocalRandom.nextInt(cities.size()), cities.size() % 3);
		List<TransportTraceHistory> histories = new ArrayList<>();
		int pre = 0;
		int num = 0;
		TransportTraceHistory preStep = null;
		TransportTraceHistory history = null;
		int interval = 0;
		for (int i = 1; i <= count; i++) {
			history = new TransportTraceHistory();
			num = generateRandomNum(pre, cities.size() - 1);
			if (num == pre) {
				num += 1;
				if (num == cities.size() - 1) {
					break;
				}
			}
			City city = cities.get(num);
			history.setPlanId(plan.getId());
			history.setCurrentRegion(preStep == null ? plan.getOrigin() : preStep.getNextRegion());
			history.setNextRegion(city.getAdcode());
			history.setReachTime(getTime(interval));
			history.setLeaveTime(getTime(interval + 4));
			preStep = history;
			pre = num;
			interval += generateRandomNum(8, 24);
			histories.add(history);
		}
		history = new TransportTraceHistory();
		history.setPlanId(plan.getId());
		history.setCurrentRegion(plan.getDestination());
		LocalDateTime localDateTime = plan.getPlanReachTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		localDateTime = localDateTime.withHour(threadLocalRandom.nextInt(24));
		localDateTime = localDateTime.withMinute(threadLocalRandom.nextInt(60));
		Date reachTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		history.setReachTime(reachTime);
		histories.add(history);
		transportTraceHistoryService.saveBatch(histories);
		redisTemplate.opsForValue().set(RedisConstant.TRACE_INFO_PREFIX + plan.getId(), histories);
		// TODO 通知派件
		rabbitTemplate.convertAndSend("dispatch.order.exchange", "dispatch.order", plan.getId());
	}


	public int generateRandomNum(int pre, int bound) {
		ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
		int num = threadLocalRandom.nextInt(bound);
		return (num >= pre ? num : generateRandomNum(pre, bound));
	}

	public synchronized Date getTime(int interval) {
		LocalDateTime now = LocalDateTime.now();
		now = now.plusHours(interval);
		now = now.withMinute(ThreadLocalRandom.current().nextInt(60))
				.withSecond(ThreadLocalRandom.current().nextInt(60));
		return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
	}


	public static void main(String[] args) {
		File file = new File("D:\\appsdata\\temp\\test\\cities.json");
		String res = FileUtil.readString(file, StandardCharsets.UTF_8);
		List<City> cities = JSON.parseObject(res, new TypeReference<List<City>>() {
		});
		cities = cities.stream().distinct().collect(Collectors.toList());
		System.out.println(cities);
	}


	@Data
	@AllArgsConstructor
	public static class Node {


		private String orderId;

		private String regionCode;

	}


}
