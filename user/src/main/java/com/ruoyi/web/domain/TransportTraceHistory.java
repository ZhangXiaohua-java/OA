package com.ruoyi.web.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 运输计划的追踪信息表
 *
 * @TableName t_transport_trace_history
 */

@Data
public class TransportTraceHistory implements Serializable {
	/**
	 *
	 */
	private Integer id;

	/**
	 * 运输计划的id
	 */
	private Integer planId;


	/**
	 * 到达时间
	 */
	private Date reachTime;

	/**
	 * 离开时间
	 */
	private Date leaveTime;


	/*  当前站点信息 */
	private String currentRegion;


	/* 下一个站点信息 */
	private String nextRegion;

	private static final long serialVersionUID = 1L;
}