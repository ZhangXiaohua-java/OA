package com.ruoyi.web.component;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.web.to.PoiResult;
import com.ruoyi.web.to.Pois;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author 张晓华
 * @date 2023-3-12
 */
@Component
public class PoiComponent {

	@Resource
	private CloseableHttpClient httpClient;

	private static final String url = "https://restapi.amap.com/v5/place/detail";

	private static final String key = "b03fe8c955886c7fbafac0adf0ed4104";


	public Pois searchLocationInfoByPoiCode(String poiCode) throws IOException {
		String requestUrl = url + "?" + "key=" + key + "&id=" + poiCode;
		HttpGet request = new HttpGet(requestUrl);
		CloseableHttpResponse httpResponse = httpClient.execute(request);
		String result = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
		PoiResult poiResult = JSON.parseObject(result, PoiResult.class);
		if (poiResult.getInfo().equals("OK")) {
			return poiResult.getPois().get(0);
		} else {
			return null;
		}
	}


}
