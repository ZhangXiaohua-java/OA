package cn.edu.huel.user.component;

import cn.edu.huel.user.domain.PoiResult;
import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 张晓华
 * @date 2023-3-12
 */
@Component
public class PoiComponent {

	@Resource
	private CloseableHttpClient httpClient;

	private static final String url = "https://restapi.amap.com/v5/place/text";

	private static final String key = "b03fe8c955886c7fbafac0adf0ed4104";


	public PoiResult searchPoi(String locationName, String countyCode) throws IOException {
		HashMap<String, String> params = new HashMap<>();
		params.put("key", key);
		params.put("keywords", locationName);
		params.put("region", countyCode);
		params.put("city_limit", "true");
		params.put("page_size", "25");
		String baseUrl = url;
		baseUrl = concatUrl(baseUrl, params);
		HttpGet request = new HttpGet(baseUrl);
		CloseableHttpResponse httpResponse = httpClient.execute(request);
		String str = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
		PoiResult poiResult = JSON.parseObject(str, PoiResult.class);
		System.out.println(poiResult);
		return poiResult;
	}


	public String concatUrl(String baseUrl, Map<String, String> params) {
		Set<String> keySet = params.keySet();
		int index = 0;
		for (String key : keySet) {
			if (index == 0) {
				baseUrl += "?" + key + "=" + params.get(key);
			} else {
				baseUrl += "&" + key + "=" + params.get(key);
			}
			index++;
		}
		return baseUrl;
	}


}
