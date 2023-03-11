/**
 * Copyright 2023 json.cn
 */
package cn.edu.huel.user.to;

import lombok.Data;

/**
 * Auto-generated: 2023-03-09 22:24:50
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
public class RemoteVerifyMessageTo {

	private int error_code;

	private String reason;

	private Result result;

	private String sn;

}