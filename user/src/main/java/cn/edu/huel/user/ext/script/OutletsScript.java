package cn.edu.huel.user.ext.script;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 张晓华
 * @date 2023-3-1
 */
public class OutletsScript {


	public static void getOutlets() throws IOException {
		String url = "https://www.deppon.com/gwapi/queryService/eco/query/branch/queryDeptNet";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		Map<String, String> map = new HashMap<>();
		map.put("provinceCode", "410000");
		map.put("provinceName", "河南省");
		map.put("cityCode", "410200");
		map.put("cityName", "开封市");
		map.put("countyCode", "410225");
		map.put("countyName", "兰考县");
		map.put("pageIndex", "1");
		map.put("pageSize", "10");
		map.put("level", "4");
		StringEntity entity = new StringEntity(JSON.toJSONString(map), ContentType.APPLICATION_JSON);
		post.setEntity(entity);
		CloseableHttpResponse httpResponse = httpClient.execute(post);
		String result = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
		System.out.println(result);
	}

	public static void getCodes() {
		String url = "https://www.deppon.com/phonerest/citycontrol/queryProviecn/100000";
		RestTemplate restTemplate = new RestTemplate();
		String object = restTemplate.getForObject(url, String.class);
		System.out.println(object);

	}

	public static void main(String[] args) throws IOException {
		getOutlets();
	}


}
