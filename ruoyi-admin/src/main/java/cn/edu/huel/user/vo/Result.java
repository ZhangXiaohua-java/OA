package cn.edu.huel.user.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 张晓华
 * @date 2023-2-20
 */
@Data
public class Result {


	private int code;

	private String msg;

	public Map<String, Object> data;

	public Result(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static Result ok() {
		return new Result(200, "OK");
	}

	public static Result error() {
		return new Result(500, "Fail");
	}

	public static Result error(String msg) {
		return new Result(500, msg);
	}

	public static Result ok(String key, Object value) {
		Result result = ok();
		result.data = new HashMap<>();
		result.data.put(key, value);
		return result;
	}

	public Result put(String key, Object value) {
		if (this.data == null) {
			this.data = new HashMap<>();
		}
		this.data.put(key, value);
		return this;
	}


}
