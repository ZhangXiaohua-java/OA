package cn.edu.huel.user.to;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * @author 张晓华
 * @date 2022-12-14
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeTo implements Serializable {

	private String out_trade_no;

	private String product_code = "FAST_INSTANT_TRADE_PAY";

	private String total_amount;

	private String subject;

	private String body;

	// 默认的支付超时时间
	private String timeout_express = "10m";

	@Override
	public String toString() {
		this.setOut_trade_no(new String(this.getOut_trade_no().toString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
		this.setTotal_amount(new String(this.getTotal_amount().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
		this.setBody(new String(this.getBody().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
		this.setSubject(new String(this.getSubject().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
		this.setProduct_code(new String(this.getProduct_code().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
		this.setTimeout_express(new String(this.getTimeout_express().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
		return JSON.toJSONString(this);
	}


}
