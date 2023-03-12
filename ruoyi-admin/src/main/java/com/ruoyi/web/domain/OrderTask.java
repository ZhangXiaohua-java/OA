package com.ruoyi.web.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务清单表
 *
 * @TableName t_order_task
 */
@TableName(value = "t_order_task")
@Data
public class OrderTask implements Serializable {


	/**
	 *
	 */
	@TableId
	private Long id;

	/**
	 * 订单id
	 */
	private String orderId;

	/**
	 * 负责此件的员工id
	 */
	private Long employeeId;

	/**
	 * 负责此件的员工姓名
	 */
	private String employeeName;

	/**
	 * 负责此件的营业厅id
	 */
	private String unifiedCode;

	/**
	 * 任务状态,0新建,1已确认,2已揽收
	 */
	private String status;

	/**
	 * 寄件人联系方式
	 */
	private String posterPhone;

	/**
	 * 寄件人姓名
	 */
	private String posterName;

	/**
	 * 营业员确认重量
	 */
	private Integer confirmWeight;

	/**
	 * 营业员确认体积
	 */
	private Integer confirmVolume;

	/**
	 * 营业员实际确认的运费,允许和系统计算的运费有出入
	 */
	private Integer confirmPostCost;

	/**
	 * 确认时间
	 */
	private Date confirmTime;

	/**
	 * 任务的创建时间
	 */
	private Date createTime;


	/* 寄件地址信息 */
	private String posterDetailAddress;

	/* 收件人信息 */
	private String receiverInfo;

	/* 预约时间 */
	private Date appointmentTime;

	/* 备注信息 */
	private String remark;

	/* 目的地城市编码,该字段暂时用不到 */
	private String destCountCode;

	/* 寄件地址的城市编码 */
	private String sourceCountCode;

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/* 收货地址编码信息 */
	private String poiCode;


}