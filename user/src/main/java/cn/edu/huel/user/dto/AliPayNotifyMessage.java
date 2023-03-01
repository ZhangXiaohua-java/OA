package cn.edu.huel.user.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author 张晓华
 * @date 2022-12-14
 */
@Data
public class AliPayNotifyMessage {

	private Date notify_time;

	private String notify_type;

	private String notify_id;

	private String charset;

	private String version;

	private String sign_type;

	private String sign;

	private String auth_app_id;


}
