package cn.edu.huel.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 全国地理位置信息对象 t_area
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@Data
@TableName("t_area")
public class Area  {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 父id
	 */
	@Excel(name = "父id")
	private Long pid;

	/**
	 * 简称
	 */
	@Excel(name = "简称")
	private String shortname;

	/**
	 * 名称
	 */
	@Excel(name = "名称")
	private String name;

	/**
	 * 全称
	 */
	@Excel(name = "全称")
	private String mergerName;

	/**
	 * 层级 0 1 2 省市区县
	 */
	@Excel(name = "层级 0 1 2 省市区县")
	private Long level;

	/**
	 * 拼音
	 */
	@Excel(name = "拼音")
	private String pinyin;

	/**
	 * 长途区号
	 */
	@Excel(name = "长途区号")
	private String code;

	/**
	 * 邮编
	 */
	@Excel(name = "邮编")
	private String zipCode;

	/**
	 * 首字母
	 */
	@Excel(name = "首字母")
	private String first;

	/**
	 * 经度
	 */
	@Excel(name = "经度")
	private String lng;

	/**
	 * 纬度
	 */
	@Excel(name = "纬度")
	private String lat;


}
