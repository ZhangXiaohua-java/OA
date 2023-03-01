package cn.edu.huel.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 地区表
 *
 * @TableName sys_region
 */
@TableName(value = "sys_region")
@Data
public class Region implements Serializable {
	/**
	 * 地区主键编号
	 */
	@TableId
	private String regionId;

	/**
	 * 地区名称
	 */
	private String regionName;

	/**
	 * 地区缩写
	 */
	private String regionShortName;

	/**
	 * 行政地区编号
	 */
	private String regionCode;

	/**
	 * 地区父id
	 */
	private String regionParentId;

	/**
	 * 地区级别 1-省、自治区、直辖市 2-地级市、地区、自治州、盟 3-市辖区、县级市、县
	 */
	private Integer regionLevel;

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

}