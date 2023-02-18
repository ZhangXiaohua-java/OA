package cn.edu.huel.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
 * 赔付信息对象 t_compensate
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@Data
@TableName("t_compensate")
public class Compensate {
	private static final long serialVersionUID = 1L;

	/**
	 * 使用nanoid作为主键
	 */
	@TableId(type = IdType.INPUT)
	private String id;

	/**
	 * 订单号
	 */
	@Excel(name = "订单号")
	private String orderId;

	/**
	 * 顾客id
	 */
	@Excel(name = "顾客id")
	private String customerId;

	/**
	 * 赔付金额
	 */
	@Excel(name = "赔付金额")
	private Long pay;

	/**
	 * 0未赔付,1沟通中,2已完成,3为止
	 */
	@Excel(name = "0未赔付,1沟通中,2已完成,3为止")
	private String status;

	/**
	 * 赔付最终处理人姓名
	 */
	@Excel(name = "赔付最终处理人姓名")
	private String employeeName;

	/**
	 * 责任人id
	 */
	@Excel(name = "责任人id")
	private Long responsibileEmployee;

}
