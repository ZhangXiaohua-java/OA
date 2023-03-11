package cn.edu.huel.user.controller;

import cn.edu.huel.security.vo.Result;
import cn.edu.huel.user.base.constant.RedisConstant;
import cn.edu.huel.user.domain.PostOrder;
import cn.edu.huel.user.domain.TransferFactory;
import cn.edu.huel.user.domain.TransportTraceHistory;
import cn.edu.huel.user.service.IPostOrderService;
import cn.edu.huel.user.service.RegionService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.ruoyi.common.core.controller.BaseController;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
		ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
		ParserConfig.getGlobalInstance().addAccept("com.ruoyi.web.domain.TransportTraceHistory");
		if (redisTemplate.hasKey(key)) {
			JSONArray array = (JSONArray) valueOps.get(key);
			ArrayList<TransportTraceHistory> histories = new ArrayList<>();
			// TODO 草拟吗,坑逼玩意
			for (Object o : array) {
				TransportTraceHistory history = JSON.parseObject(o.toString(), TransportTraceHistory.class);
				Integer region = (Integer) JSONObject.parseObject(o.toString()).get("currentRegion");
				history.setCurrentRegion(region);
				histories.add(history);
			}
			histories.stream()
					.forEach(e -> {
						TransferFactory transferFactory = null;
						Object o = valueOps.get(RedisConstant.TRANSFER_INFO_PREFIX + e.getCurrentRegion());
						if (Objects.nonNull(o)) {
							transferFactory = JSON.parseObject(o.toString(), TransferFactory.class);
						}
						e.setPreDescInfo(transferFactory == null ? "" : transferFactory.getName());
						o = valueOps.get(RedisConstant.TRANSFER_INFO_PREFIX + e.getNextRegion());
						if (Objects.nonNull(o)) {
							transferFactory = JSON.parseObject(o.toString(), TransferFactory.class);
						}
						e.setNextDescInfo(transferFactory == null ? "" : transferFactory.getName());
					});
			return Result.ok().put("data", histories);
		}
		return Result.error("no data");
	}


}
