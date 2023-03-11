package com.ruoyi.web.schedule;

import cn.edu.huel.user.domain.Region;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ruoyi.web.constant.RedisConstant;
import com.ruoyi.web.domain.Node;
import com.ruoyi.web.domain.TransferFactory;
import com.ruoyi.web.service.TransferFactoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 张晓华
 * @date 2023-3-7
 */
@Slf4j
@Component
public class TransferDataScript {


	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Resource
	TransferFactoryService transferFactoryService;

	public void initial() {

		Set<String> keys = redisTemplate.keys(RedisConstant.REGION_INFO_PREFIX + "*");
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		log.info("查询到的keys{}", keys);
		File file = new File("D:\\workbench\\Java\\idea\\design-pattern\\dp\\target\\classes\\capital.json");
		String str = FileUtil.readString(file, StandardCharsets.UTF_8);
		List<Node> nodes = JSON.parseObject(str, new TypeReference<List<Node>>() {
		});
		for (String key : keys) {
			if (key.contains("child")) {
				continue;
			}
			Region region = (Region) ops.get(key);
			TransferFactory factory = new TransferFactory();
			factory.setLeaderName("张晓华");
			List<Node> list = nodes.stream().filter(e -> e.getCapitalName().equals(region.getRegionName())).collect(Collectors.toList());
			factory.setRegionCode(region.getRegionCode());
			factory.setParentId(0);
			factory.setName(region.getRegionName().replace("市", "") + "转运场");
			if (list != null && list.size() != 0) {
				factory.setLevel("1");
			} else {
				factory.setLevel("2");
			}
			transferFactoryService.save(factory);
		}
	}


	public void setParentId() {
		File file = new File("D:\\workbench\\Java\\idea\\design-pattern\\dp\\target\\classes\\capital.json");
		String str = FileUtil.readString(file, StandardCharsets.UTF_8);
		List<Node> nodes = JSON.parseObject(str, new TypeReference<List<Node>>() {
		});
		List<TransferFactory> factories = transferFactoryService.queryAllLevel2Factories();
		List<String> codes = factories.stream().map(e -> e.getRegionCode()).collect(Collectors.toList());
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		Set<String> keys = redisTemplate.keys("region:province:info:*");
		List<Region> provinces = keys.stream().map(e -> (Region) ops.get(e)).collect(Collectors.toList());
		List<Region> regions = codes.stream().map(e -> (Region) ops.get(RedisConstant.REGION_INFO_PREFIX + e)).collect(Collectors.toList());
		for (TransferFactory factory : factories) {
			String regionCode = factory.getRegionCode();
			Region reg = regions.stream().filter(e -> e.getRegionCode().equals(regionCode))
					.collect(Collectors.toList()).get(0);
			String parentId = reg.getRegionParentId();
			Region parent = provinces.stream().filter(e -> e.getRegionId().equals(parentId)).collect(Collectors.toList()).get(0);
			Node node = nodes.stream().filter(e -> e.getProvinceName().equals(parent.getRegionName())).collect(Collectors.toList()).get(0);
			String capitalName = node.getCapitalName();
			capitalName = capitalName.replace("市", "");
			TransferFactory parentFactory = transferFactoryService.fuzzyQueryFactory(capitalName);
			if (parentFactory == null) {
				continue;
			}
			factory.setParentId(parentFactory.getId());
			transferFactoryService.updateById(factory);
		}
	}


	@PostConstruct
	public void putData() {
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		List<TransferFactory> list = transferFactoryService.list();
		list.stream()
				.forEach(e -> {
					ops.set(RedisConstant.TRANSFER_INFO_PREFIX + e.getId(), e, Duration.ofDays(1));
				});
	}


}
