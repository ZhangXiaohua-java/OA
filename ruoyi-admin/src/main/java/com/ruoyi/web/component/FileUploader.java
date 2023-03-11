package com.ruoyi.web.component;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.ruoyi.web.config.AliOssConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author 张晓华
 * @date 2023-3-9
 */
@Component
public class FileUploader {


	@Resource
	private OSS oss;

	@Resource
	private AliOssConfig config;


	public String uploadFile(String fileName, InputStream inputStream) {
		Objects.requireNonNull(fileName, "文件名不可为空");
		Objects.requireNonNull(inputStream, "流不可为空");
		PutObjectRequest request = new PutObjectRequest(config.getBucket(), fileName, inputStream);
		PutObjectResult result = oss.putObject(request);
		return "";
	}


/**
 private QiNiuConfig qiNiuConfig;

 private UploadManager uploadManager;


 private Auth auth;

 public String getToken() {
 return auth.uploadToken(qiNiuConfig.getBucket());
 }

 */

	/**
	 * @param bytes    二进制字节数据
	 * @param filename 文件名,必须包含文件后缀
	 * @return 文件上传
	public String uploadBytes(byte[] bytes, String filename) throws QiniuException {
	Response response = uploadManager.put(bytes, filename, getToken());
	if (!response.isOK()) {
	return null;
	}
	return qiNiuConfig.getPrefix() + "/" + filename;
	}
	 */

}
