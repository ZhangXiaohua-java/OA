package cn.edu.huel.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
 * 顾客收货地址列对象 t_post_address
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@Data
@TableName("t_post_address")
public class PostAddress {
	private static final long serialVersionUID = 1L;

	/**
	 * 使用nanoid作为主键
	 */
	@TableId(type = IdType.INPUT)
	private String id;

	/**
	 * 收货地址的邮编
	 */
	@Excel(name = "收货地址的邮编")
	private String postCode;

	/**
	 * 是否是默认收货地址
	 */
	@Excel(name = "是否是默认收货地址")
	private String isDefault;

	/**
	 * 收货人姓名
	 */
	@Excel(name = "收货人姓名")
	private String recevierName;

	/**
	 * 收货人手机号
	 */
	@Excel(name = "收货人手机号")
	private String phone;


}
