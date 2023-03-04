package com.ruoyi.web.controller.transport;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.constant.RedisConstant;
import com.ruoyi.web.domain.RoutePath;
import com.ruoyi.web.domain.TransportPlan;
import com.ruoyi.web.feign.FeignRemoteClient;
import com.ruoyi.web.service.TransportPlanService;
import com.ruoyi.web.vo.ConditionVo;
import com.ruoyi.web.vo.TransportPlanVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author 张晓华
 * @date 2023-3-3
 */
@Slf4j
@RestController
@RequestMapping("/transport")
public class TransportPlanController {

	@Resource
	private TransportPlanService transportPlanService;

	@Resource
	private FeignRemoteClient remoteClient;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;


	@PostMapping("/query/plan")
	public AjaxResult queryTransportPlans(@RequestBody ConditionVo conditionVo) {
		List<TransportPlan> plans = transportPlanService.queryPlansByCondition(conditionVo);
		return new AjaxResult(200, "ok").put("data", plans);
	}

	@PostMapping("/create/plan")
	public AjaxResult createNewPlan(@RequestBody @Validated TransportPlanVo planVo) {
		log.info("接收到的计划信息{}", planVo);
		String result = remoteClient.queryRegionCode(planVo.getDest());
		String code = JSON.parseObject(JSON.parseObject(result).get("data").toString())
				.get("data").toString();
		log.info("regionCode{}", code);
		planVo.setDestRegionCode(code);
		try {
			boolean res = transportPlanService.createNewPlan(planVo);
		} catch (Exception e) {
			return new AjaxResult(500, "提交的信息不合法");
		}
		return new AjaxResult(200, "ok");
	}


	@GetMapping("/preview/route/{id}")
	public AjaxResult previewRoutePath(@PathVariable Long id) {
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		Object o = ops.get(RedisConstant.TRANSPORT_PLAN_ROUTE_PATH_PREFIX + id);
		return new AjaxResult(200, "ok").put("data", o);
	}

	@PostMapping("/assign/route/path/{id}")
	public AjaxResult assignRoutePath(@PathVariable Integer id, @RequestParam Integer planId) {
		if (planId == null || planId > 2) {
			return new AjaxResult(500, "非法的参数");
		}
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		List<RoutePath> routePaths = (List<RoutePath>) ops.get(RedisConstant.TRANSPORT_PLAN_ROUTE_PATH_PREFIX + id);
		byte[] bytes = JSON.toJSONString(routePaths.get(planId)).getBytes(StandardCharsets.UTF_8);
		log.info("字节数组的长度{}", bytes.length);
		try {
			transportPlanService.assignRoutePath(id, bytes);
		} catch (Exception e) {
			return new AjaxResult(500, e.getMessage());
		}
		return new AjaxResult(200, "ok");
	}


}
