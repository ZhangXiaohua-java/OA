package cn.edu.huel.user.component;

import cn.edu.huel.user.to.RemoteVerifyMessageTo;
import cn.edu.huel.user.to.Result;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @author 张晓华
 * @date 2023-3-9
 */
@Data
@Slf4j
@Component
public class AuthComponent {


	@Resource
	private CloseableHttpClient httpClient;

	@Value("${verify.app-code}")
	private String appCode;

	/**
	 * 实名认证
	 *
	 * @param idCard   身份证号
	 * @param realName 真实姓名
	 * @param image    大头照
	 */
	public boolean doAuth(String idCard, String realName, String image) throws IOException {
		HttpPost post = new HttpPost("https://zfa.market.alicloudapi.com/efficient/idfaceIdentity");
		post.addHeader("Authorization", "APPCODE " + appCode);
		post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		ArrayList<NameValuePair> list = new ArrayList<>();
		list.add(new BasicNameValuePair("number", idCard));
		list.add(new BasicNameValuePair("name", realName));
		list.add(new BasicNameValuePair("base64Str", image));
		list.add(new BasicNameValuePair("liveChk", "0"));
		list.add(new BasicNameValuePair("Threshold", "0.33"));
		post.setEntity(new UrlEncodedFormEntity(list, StandardCharsets.UTF_8));
		CloseableHttpResponse httpResponse = httpClient.execute(post);
		if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			return false;
		}
		String res = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
		RemoteVerifyMessageTo message = JSON.parseObject(res, RemoteVerifyMessageTo.class);
		if (message.getError_code() != 0) {
			return false;
		}
		Result result = message.getResult();
		log.info("实名验证的结果{}", result);
		return result.getValidate_Result() == 1 && result.getSimilarity().compareTo(45.0) > 0;
	}


}
