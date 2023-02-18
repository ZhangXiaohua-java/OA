package cn.edu.huel.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;

/**
 * 邮寄货品历史对象 t_post_history
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@TableName("t_post_history")
public class PostHistory {
	private static final long serialVersionUID = 1L;

	/**
	 * 使用雪花id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * 顾客id
	 */
	@Excel(name = "顾客id")
	private String customerId;

	/**
	 * 订单号
	 */
	@Excel(name = "订单号")
	private String orderId;

	/**
	 * 货品邮寄状态,0下单未发出,1已发出,2运输中,3已运达,4已签收,5售后中,6为止状态,7丢失
	 */
	@Excel(name = "货品邮寄状态,0下单未发出,1已发出,2运输中,3已运达,4已签收,5售后中,6为止状态,7丢失")
	private String status;


}
