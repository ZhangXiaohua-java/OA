package cn.edu.huel.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 顾客个人信息对象 t_customer
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@Data
@TableName("t_customer")
public class Customer {
	private static final long serialVersionUID = 1L;

	/**
	 * 使用雪花id作为主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * 顾客联系手机号
	 */
	@Excel(name = "顾客联系手机号")
	private String phone;

	/**
	 * 顾客登陆密码
	 */
	@Excel(name = "顾客登陆密码")
	private String password;

	/**
	 * 0女,1男
	 */
	@Excel(name = "0女,1男")
	private String gender;

	/**
	 * 顾客的真实姓名
	 */
	@Excel(name = "顾客的真实姓名")
	private String customerName;

	/**
	 * 用户身份证号,如果为空则不允许邮寄任何物品
	 */
	@Excel(name = "用户身份证号,如果为空则不允许邮寄任何物品")
	private String idNum;

	/**
	 * 用户头像地址
	 */
	@Excel(name = "用户头像地址")
	private String avatar;

	/**
	 * 用户上次登陆的ip地址,后续可做异常登陆处理等.
	 */
	@Excel(name = "用户上次登陆的ip地址,后续可做异常登陆处理等.")
	private String loginIp;

	/**
	 * 上次登陆时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "上次登陆时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date loginDate;

	/**
	 * 注册时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "注册时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date registerDate;

	/**
	 * 会员等级,0-9,后面再处理
	 */
	@Excel(name = "会员等级,0-9,后面再处理")
	private String level;


}
