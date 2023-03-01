package cn.edu.huel.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 顾客积分对象 t_integral
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@Data
@TableName("t_integral")
public class Integral {
	private static final long serialVersionUID = 1L;

	/**
	 * 雪花id做主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * 顾客id
	 */
	@Excel(name = "顾客id")
	private String customerId;

	/**
	 * 本次消费后的积分
	 */
	@Excel(name = "本次消费后的积分")
	private Long currentIntegral;

	/**
	 * 本次消费前的积分
	 */
	@Excel(name = "本次消费前的积分")
	private Long lastIntegral;

	/**
	 * 对应的订单号id
	 */
	@Excel(name = "对应的订单号id")
	private String orderId;

	/* 每次的更新时间 */
	public Date updateTime;


}
