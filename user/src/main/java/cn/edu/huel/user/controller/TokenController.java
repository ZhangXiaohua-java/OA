package cn.edu.huel.user.controller;

import cn.edu.huel.base.JwtUtils;
import cn.edu.huel.security.config.JWTConfig;
import cn.edu.huel.security.domain.User;
import cn.edu.huel.security.vo.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张晓华
 * @date 2023-2-20
 */
@Slf4j
@RequestMapping("/token")
@RestController
public class TokenController {

	@Resource
	private JWTConfig jwtConfig;

	@PostMapping("/check")
	public Result verifyToken(String token) {
		try {
			User user = JwtUtils.decode(token, jwtConfig.getSignKey(), User.class);
			if (!ObjectUtils.isEmpty(user)) {
				return Result.ok();
			}
		} catch (Exception e) {
			log.error("token验证失败{}", e.getMessage());
		}
		return Result.error();
	}


}
