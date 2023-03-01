/**
 * Copyright 2023 json.cn
 */
package cn.edu.huel.user.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MessageDto {

	private String authTradePayMode;
	private String bkagentRespInfo;
	private String buyerLogonId;
	private String buyerPayAmount;
	private String buyerUserId;
	private String buyerUserName;
	private String buyerUserType;
	private String chargeAmount;
	private String chargeFlags;
	private String code;
	private String discountAmount;
	private String discountGoodsDetail;
	private String enterprisePayInfo;
	private String errorCode;
	private String invoiceAmount;
	private String mdiscountAmount;
	private String msg;
	private String openId;
	private String outTradeNo;
	private Params params;
	private String passbackParams;
	private String payAmount;
	private String payCurrency;
	private String pointAmount;
	private String receiptAmount;
	private String receiptCurrencyType;
	private Date sendPayDate;
	private String subMsg;
	private String subject;
	private boolean success;
	private String terminalId;
	private String totalAmount;
	private String tradeNo;
	private String tradeSettleInfo;
	private String tradeStatus;


}