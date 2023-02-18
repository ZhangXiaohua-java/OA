package cn.edu.huel.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;

/**
 * 运输历史对象 t_transport
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@TableName("t_transport")
public class Transport {
	private static final long serialVersionUID = 1L;

	/**
	 * 雪花id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * 顾客id
	 */
	@Excel(name = "顾客id")
	private String customerId;

	/**
	 * 订单号,订单号就作为用户查询物流信息的唯一凭证
	 */
	@Excel(name = "订单号,订单号就作为用户查询物流信息的唯一凭证")
	private String orderId;

	/**
	 * 货品所在地址的邮编
	 */
	@Excel(name = "货品所在地址的邮编")
	private String currentPosition;

	/**
	 * 操作的货品的员工id
	 */
	@Excel(name = "操作的货品的员工id")
	private Long employeeId;

	/**
	 * 员工姓名,冗余信息,减少查表次数
	 */
	@Excel(name = "员工姓名,冗余信息,减少查表次数")
	private String employeeName;


}
