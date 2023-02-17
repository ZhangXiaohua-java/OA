package cn.edu.huel.test;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author 张晓华
 * @date 2023-2-17
 */

public class AnyTest {



	@Test
	public void id() {
		Snowflake snowflake = IdUtil.getSnowflake(1, 1);
		long id = snowflake.nextId();
		System.out.println(id);
		System.out.println((id +"").length());
	}

	@Test
	public void password() {
		String encode = new BCryptPasswordEncoder().encode("123456");
		System.out.println(encode);
		System.out.println(encode.length());
	}

	@Test
	public void nanoid() {
		String nanoId = IdUtil.nanoId();
		System.out.println(nanoId);
		System.out.println(nanoId.length());
	}

}
