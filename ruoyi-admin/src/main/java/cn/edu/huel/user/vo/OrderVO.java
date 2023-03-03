package cn.edu.huel.user.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author 张晓华
 * @date 2023-2-23
 */
@Data
public class OrderVO {


	private String posterName;

	private String posterPhone;


	private String receiverName;


	private String receiverPhone;

	/* 寄件地址的城市邮编 */

	private String postCode;

	/* 收件地址的城市邮编 */
	private String receiveCode;

	/* 上门揽件的详细带地址 */

	private String detailAddress;

	/* 收件的详细地址 */
	private String dispatchDetailAddress;


	private String goodsName;

	private Long weight;

	private Double volume;

	/* 保价 */
	private Integer insure;

	/* 是否需要签收单 */
	private boolean receipt;

	/* 签收单类型 */
	private String receiptType;

	/* 支付方式 */
	private Integer payType;

	/* 预约的上门揽件时间 */
	private Date appointmentTime;

	/* 邮寄地址的邮编 */
	private String posterZipCode;

	/* 收件地址的邮编 */

	private String receiveZipCode;


	/* 签收口令 */
	private String token;

	/* 订单备注信息 */
	private String remark;


	private String customerId;


	/* 订单号 */
	private String orderId;

	/* 营业网点唯一识别码 */
	private String unifiedCode;

	/* 区域代码,用来替换之前的zipcode邮编,使用邮编表示地域过于局限... */
	private String countyCode;

}
