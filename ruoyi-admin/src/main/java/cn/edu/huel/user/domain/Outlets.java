package cn.edu.huel.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 网点信息表
 *
 * @TableName t_outlets
 */
@Data
public class Outlets implements Serializable {
	/**
	 * 自增id即可
	 */
	private Integer id;

	/**
	 * 营业部名字
	 */
	private String outletsName;

	/**
	 * 网点的经纬度坐标信息
	 */
	private String position;

	/**
	 * 营业厅联系电话
	 */
	private String phone;

	/**
	 * 业务范围,如,发货 快递派送 快递自提 零担派送 零担自提
	 */
	private String serviceProvide;

	/**
	 * 详细地址
	 */
	private String detailAddress;

	/**
	 * 所在城市的邮编
	 */
	private String postCode;

	/**
	 * 营业部负责人id
	 */
	private Long leaderId;

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
}