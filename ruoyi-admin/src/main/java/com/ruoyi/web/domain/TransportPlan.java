package com.ruoyi.web.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 运输计划表
 *
 * @TableName t_transport_plan
 */
@TableName(value = "t_transport_plan")
@Data
public class TransportPlan implements Serializable {
	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 出发地城市编码+转运场id
	 */
	private String origin;

	/**
	 * 目的地城市代码+转运场id
	 */
	private String destination;

	/**
	 * 计划出发时间
	 */
	private Date planStartTime;

	/**
	 * 计划到达时间
	 */
	private Date planReachTime;

	/**
	 * 计划的运输时效,多久送达
	 */
	private String term;

	/**
	 * 实际出发时间
	 */
	private Date leaveTime;

	/**
	 * 实际到达时间
	 */
	private Date reachTime;

	/**
	 * 计划的创建时间
	 */
	private Date createTime;

	/**
	 * 计划的修改时间,计划时间到达后就不可修改
	 */
	private Date updateTime;

	/**
	 * 计划的创建者id
	 */
	private Long createBy;

	/**
	 * 运输计划的详情id,一对一
	 */
	private Integer detailId;

	/* 计划的状态,1新建,2已经生成了路径信息,3已经分配了路径信息,4已发出,5运行中,6已完成 */
	private char status;

	/* 路线信息,用Blob存储的Json二进制信息,可以还原成RoutePath对象 */
	private byte[] routePath;

	/* 数据库中不存在这个字段,这个字段是routePath的具体信息,方便使用而已 */
	@TableField(exist = false)
	private RoutePath path;


	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
}