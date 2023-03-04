package com.ruoyi.web.vo;

import com.ruoyi.web.validator.DateTime;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author 张晓华
 * @date 2023-3-3
 */
@Data
public class TransportPlanVo {


	/* 目的地 */
	@NotNull(message = "目的地不可为空")
	private Long[] dest;

	/*  时效 */
	@NotNull(message = "时效不可为空")
	@Max(value = 7, message = "超过了最大时效时长")
	@Min(value = 1, message = "无效的时效")
	private Integer term;

	/* 运输方式 */
	@NotNull(message = "运输方式不可为空")
	@Max(value = 2, message = "不存在的运输方式")
	@Min(value = 1, message = "不存在的运输方式")
	private Integer way;

	/* 出发时间 */
	@Future(message = "已超出出发时间")
	private Date leaveTime;

	/* 预计到达时间 */
	@Future(message = "无效的到达时间")
	@DateTime(message = "无效的到达时间")
	private Date reachTime;

	/* 容量,体积*/
	@NotNull
	@Max(value = 1000, message = "无效的容量")
	private Integer capacity;

	/* 重量 */
	@NotNull
	@Max(value = 1000, message = "无效的重量")
	private Integer weight;

	private String destRegionCode;

	@NotNull(message = "部门信息不可为空")
	@Min(value = 1, message = "非法的部门信息")
	private Long deptId;


	@NotNull(message = "负责人信息不可为空")
	@Min(value = 1, message = "非法的负责人信息")
	private Long employeeId;


}
