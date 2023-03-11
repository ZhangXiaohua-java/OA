package cn.edu.huel.user.controller;

import cn.edu.huel.security.vo.Result;
import cn.edu.huel.user.base.utils.Base64Utils;
import cn.edu.huel.user.component.AuthComponent;
import cn.edu.huel.user.component.FileUploader;
import cn.edu.huel.user.config.AliOssConfig;
import cn.edu.huel.user.domain.Customer;
import cn.edu.huel.user.service.ICustomerService;
import cn.edu.huel.user.vo.AuthVo;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author 张晓华
 * @date 2023-3-9
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Resource
	private FileUploader fileUploader;

	@Resource
	private CloseableHttpClient httpClient;

	@Resource
	private AuthComponent authComponent;

	@Resource
	private ICustomerService customerService;

	Snowflake snowflake = IdUtil.getSnowflake();

	@Resource
	private AliOssConfig ossConfig;

	@Resource
	private Executor executor;

	@PostMapping("/doAuth")
	public Result verify(@Valid AuthVo authVo) throws IOException {
		Objects.requireNonNull(authVo.getFile(), "file can't be null");
		byte[] bytes = authVo.getFile().getInputStream().readAllBytes();
		String base64Str = Base64Utils.imageToStr(bytes);
		log.info("接收到的参数{}", authVo);
		boolean res = authComponent.doAuth(authVo.getIdNum(), authVo.getName(), base64Str);
		String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (res) {
			MultipartFile[] images = authVo.getCardImages();
			CompletableFuture<String> futureOne = CompletableFuture.supplyAsync(() -> {
				try {
					String fileName = "user/" + snowflake.nextIdStr() + ".jpg";
					fileUploader.uploadFile(fileName, images[0].getInputStream());
					return ossConfig.getPrefix() + fileName;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}, executor);
			CompletableFuture<String> futureTwo = CompletableFuture.supplyAsync(() -> {
				try {
					String fileName = "user/" + snowflake.nextIdStr() + ".jpg";
					fileUploader.uploadFile(fileName, images[1].getInputStream());
					return ossConfig.getPrefix() + fileName;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}, executor);
			futureTwo.thenAcceptBothAsync(futureOne, (str1, str2) -> {
				// TODO 上传身份证正反面信息,并存入到数据库中的auth_info字段中
				// 保存面部特征值
				Customer customer = new Customer();
				customer.setId(id);
				customer.setFacialFeatures(base64Str.getBytes(StandardCharsets.UTF_8));
				customer.setAuthInfo(str1.concat(",").concat(str2));
				customer.setAuthStatus(true);
				customerService.updateAuthInfo(customer);
			}).exceptionally(e -> {
				e.printStackTrace();
				log.error("监测到了异步任务中的错误消息{}", e.getMessage());
				return null;
			});
		}
		return Result.ok();
	}


}
