package com.ruoyi.web.to;

import lombok.Data;

/**
 * @author 张晓华
 * @date 2023-2-27
 */
@Data
public class OrderDTO {

	/* 系统生成的订单号 */
	private String orderID;

	/* 订单的城市代码 */
	private String countCode;

	/* 营业网点id */
	private String unifiedCode;

	/*客户的详细地址*/
	private String detailAddress;

	/*客户的联系方式*/
	private String phone;


}
