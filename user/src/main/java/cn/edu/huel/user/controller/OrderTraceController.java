package cn.edu.huel.user.controller;

import cn.edu.huel.security.vo.Result;
import cn.edu.huel.user.base.constant.RedisConstant;
import cn.edu.huel.user.domain.PostOrder;
import cn.edu.huel.user.service.IPostOrderService;
import cn.edu.huel.user.service.RegionService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.web.domain.TransportTraceHistory;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 运输历史Controller
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@RestController
@RequestMapping("/trace")
public class OrderTraceController extends BaseController {


	@Resource
	private IPostOrderService orderService;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	private ListOperations<String, Object> ops;

	@Resource
	private RegionService regionService;

	@PostConstruct
	public void init() {
		ops = redisTemplate.opsForList();
	}

	@GetMapping("/{orderId}")
	public Result countCost(@PathVariable String orderId) {
		return Result.ok();
	}


	@PostMapping("/{id}")
	public Result queryTraceInfo(@PathVariable Long id) {
		PostOrder order = orderService.getById(id);
		if (Objects.isNull(order)) {
			return Result.error("no data");
		}
		Integer traceId = order.getTraceId();
		String key = RedisConstant.TRACE_INFO_PREFIX + traceId;
		if (redisTemplate.hasKey(key)) {
			Object o = redisTemplate.opsForValue().get(key);
			List<TransportTraceHistory> histories = JSON.parseObject(o.toString(), new TypeReference<List<TransportTraceHistory>>() {
			});
			System.out.println(histories);
			histories.stream()
					.forEach(e -> {
						e.setCurrentRegion(regionService.getNameByRegionCode(e.getCurrentRegion()));
						e.setNextRegion(regionService.getNameByRegionCode(e.getNextRegion()));
					});
			return Result.ok().put("data", histories);
		}
		return Result.error("no data");
	}


}
