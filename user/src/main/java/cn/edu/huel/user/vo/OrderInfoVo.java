package cn.edu.huel.user.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 张晓华
 * @date 2023-2-27
 */
@Data
public class OrderInfoVo {
	/**
	 * 雪花算法生成id
	 */
	private String id;

	/**
	 * 顾客id
	 */
	private String customerId;

	/**
	 * 支付金额
	 */
	private BigDecimal pay;

	/**
	 * 积分抵扣金额,可以选择是否使用
	 */
	private BigDecimal discount;

	/**
	 * 本次支付是否使用了积分,0未使用,1使用
	 */
	private String useDiscount;

	/**
	 * 支付时间,下单即支付
	 */
	private Date orderTime;

	/**
	 * 运输商品的类型,后续会设计出一个字典表出来
	 */
	private Long goodsType;

	/**
	 * 创建订单的员工姓名,冗余信息
	 */
	private String employeeName;

	/* 始发地,475300,详细地址 */
	private String origin;

	/* 目的地,475300,详细地址 */
	private String dest;

	/* 收件人姓名 */
	private String receiverName;

	/* 收件人手机号 */
	private String receiverPhone;

	/* 订单状态,具体的表述见OrderStatusEnum */
	private char status;



}
