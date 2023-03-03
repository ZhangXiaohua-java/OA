package com.ruoyi.web.controller.transport;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.domain.TransportPlan;
import com.ruoyi.web.feign.FeignRemoteClient;
import com.ruoyi.web.service.TransportPlanService;
import com.ruoyi.web.vo.ConditionVo;
import com.ruoyi.web.vo.TransportPlanVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
		boolean res = transportPlanService.createNewPlan(planVo);
		return new AjaxResult(200, "ok");
	}


}
