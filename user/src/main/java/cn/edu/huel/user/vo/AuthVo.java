package cn.edu.huel.user.vo;

import cn.edu.huel.user.base.validator.FileSizeCheck;
import cn.edu.huel.user.base.validator.IdCheck;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 张晓华
 * @date 2023-3-9
 */
@Data
public class AuthVo {

	@NotNull(message = "请上传大头照")
	@FileSizeCheck
	private MultipartFile file;

	@NotNull(message = "身份证照片信息不可为空")
	@Size(min = 2, max = 2, message = "身份证照片信息不可为空")
	private MultipartFile[] cardImages;


	@IdCheck(message = "无效的身份证号")
	@NotNull(message = "身份证号不允许为空")
	private String idNum;


	@NotNull(message = "姓名不可为空")
	private String name;

}
