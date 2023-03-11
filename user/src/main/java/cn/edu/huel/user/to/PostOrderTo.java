package cn.edu.huel.user.to;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单对象 t_post_order
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@Data
public class PostOrderTo {

	/**
	 * 雪花算法生成id
	 */
	@TableId(type = IdType.ASSIGN_ID)
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
	private Integer status;


	/* 订单的创建者,信息有点多余了 */
	private Long createBy;


	/* 物品的重量 */
	private Integer weight;


	/* 物品的体积,用于核算运费 */
	private Integer volume;

	/*  对应着物流运输计划的id,方便追踪位置信息  */
	private Integer traceId;

	/* 寄件人姓名 */
	private String posterName;

	/* 寄件人手机号 */
	private String posterPhone;


}
