package com.ruoyi.web.controller.dispatch;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiniu.util.Auth;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.component.FileUploader;
import com.ruoyi.web.config.AliOssConfig;
import com.ruoyi.web.domain.DispatchOrderInfo;
import com.ruoyi.web.service.DispatchOrderInfoService;
import com.ruoyi.web.vo.ConditionVo;
import com.ruoyi.web.vo.DispatchInfoVo;
import com.ruoyi.web.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @author 张晓华
 * @date 2023-3-7
 */
@Slf4j
@RestController
@RequestMapping("/dispatch")
public class DispatchController {

	@Resource
	private DispatchOrderInfoService dispatchOrderInfoService;

	@Resource
	private Auth auth;

	@Resource
	private FileUploader fileUploader;

	@Resource
	private AliOssConfig config;

	Snowflake snowflake = IdUtil.getSnowflake();


	@GetMapping("/query")
	public AjaxResult queryDispatchOrders(ConditionVo conditionVo) {
		Page<DispatchOrderInfo> page = dispatchOrderInfoService.queryAllDispatchOrders(conditionVo);
		PageVo vo = PageVo.getPage(page);
		return new AjaxResult(200, "ok").put("data", page.getRecords()).put("page", vo);
	}


	@PostMapping("/confirm")
	public AjaxResult confirmDispatchOrders(@RequestBody DispatchInfoVo vo) throws IOException {
		// TODO 先更新状态,再保存签名信息
		boolean result = dispatchOrderInfoService.confirmOrder(vo.getOrderId());
		if (!result) {
			return new AjaxResult(500, "fail");
		}
		String str = vo.getStr();
		str = str.substring(str.indexOf(",", 1) + 1);
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] bytes = decoder.decode(str);
		for (int i = 0; i < bytes.length; ++i) {
			if (bytes[i] < 0) {
				bytes[i] += 256;
			}
		}
		String fileName = "oa/" + snowflake.nextId() + ".jpg";
		ByteArrayInputStream inputStream = IoUtil.toStream(bytes);
		fileUploader.uploadFile(fileName, inputStream);
		String url = config.getPrefix() + "/" + fileName;
		log.info("签名信息地址{}", url);
		if (dispatchOrderInfoService.updateSignInfo(vo.getOrderId(), url)) {
			return new AjaxResult(200, "ok");
		}
		return new AjaxResult(500, "出库失败,请重试");
	}


	@GetMapping("/query/{id}")
	public AjaxResult queryDetail(@PathVariable String id) {
		DispatchOrderInfo info = dispatchOrderInfoService.queryDetailById(id);
		return new AjaxResult(200, "ok").put("data", info);
	}


}
