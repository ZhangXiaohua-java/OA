package cn.edu.huel.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 运输历史对象 t_transport
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@TableName("t_order_trace")
@Data
public class OrderTrace {
	private static final long serialVersionUID = 1L;

	/**
	 * 雪花id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;


	/**
	 * 订单号,订单号就作为用户查询物流信息的唯一凭证
	 */
	@Excel(name = "订单号,订单号就作为用户查询物流信息的唯一凭证")
	private String orderId;

	/**
	 * 当前位置,只有在处于派件中状态时才有意义
	 */
	@Excel(name = "货品所在地址的邮编")
	private String currentPosition;

	/**
	 * 派件员工的id
	 */
	@Excel(name = "操作的货品的员工id")
	private Long employeeId;

	/**
	 * 派件员工姓名,冗余信息,减少查表次数
	 */
	@Excel(name = "员工姓名,冗余信息,减少查表次数")
	private String employeeName;


	/* 运输信息的追踪id,冗余字段,只是不想频繁查询订单表 */
	private Integer traceId;

	/* 状态,运输中或者是派件中,1新建,2配送中,3已完成 */
	private char status;


	private Date updateTime;




}
