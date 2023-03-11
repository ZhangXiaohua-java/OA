package cn.edu.huel.user.controller;

import cn.edu.huel.security.vo.Result;
import cn.edu.huel.user.component.FileUploader;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author 张晓华
 * @date 2023-3-9
 */
@RestController
@RequestMapping("/file")
public class FileController {

	@Resource
	private FileUploader fileUploader;

	Snowflake snowflake = IdUtil.getSnowflake();


	@PostMapping("/upload")
	public Result fileUpload(@RequestParam MultipartFile file) throws IOException {

		return Result.ok();
	}


}
