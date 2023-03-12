package com.ruoyi.web.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 待派件信息表
 *
 * @TableName t_dispatch_order_info
 */
@TableName(value = "t_dispatch_order_info")
@Data
public class DispatchOrderInfo implements Serializable {
	/**
	 *
	 */
	@TableId
	private Long id;

	/**
	 * 订单号id
	 */
	private String orderId;

	/**
	 * 寄件人姓名
	 */
	private String posterName;

	/**
	 * 寄件人手机号
	 */
	private String posterPhone;

	/**
	 * 收件人姓名
	 */
	private String receiverName;

	/**
	 * 收件人手机号
	 */
	private String receiverPhone;

	/**
	 * 寄件地址信息
	 */
	private String posterAddress;

	/**
	 * 收件地址,完整信息
	 */
	private String receiverAddress;

	/**
	 * 取件码
	 */
	private String receiverCode;

	/**
	 * 签收状态,0未签收,1已签收
	 */
	private String status;

	/**
	 * 是否已经通知过取件,0失败,1已通知
	 */
	private String notifyStatus;

	/**
	 * 签收时间
	 */
	private Date confirmTime;

	/**
	 * 负责派件的营业网点的唯一id
	 */
	private String unifiedCode;

	/**
	 * 负责此件的员工id
	 */
	private Long employeeId;

	/**
	 * 负责此件的员工姓名
	 */
	private String employeeName;


	/* 原始订单的下单时间 */
	private Date orderTime;

	/* 开始派件的时间 */
	private Date createTime;

	/**
	 * 收件人签名,照片等信息
	 */
	private String sign;


	/* 详细的收货地址信息 */
	private String poiCode;


	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
}