package cn.edu.huel.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 转运场信息表
 *
 * @TableName t_transfer_factory
 */
@Data
public class TransferFactory implements Serializable {
	/**
	 *
	 */
	private Integer id;

	/**
	 * 转运场所在的城市的编码
	 */
	private String regionCode;

	/**
	 * 转运场名字
	 */
	private String name;

	/**
	 * 转运场级别,省会城市一级转运场,地级市二级转运场
	 */
	private String level;

	/**
	 * 负责人姓名,暂时用不到
	 */
	private String leaderName;

	/**
	 * 转运场的位置信息,经度,纬度
	 */
	private String position;

	/**
	 * 转运场所在城市的邮编
	 */
	private String zipcode;

	/* 唯一识别码 */
	private String unifiedCode;


	/* 上一级转运场id */
	private Integer parentId;


	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
}