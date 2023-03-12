package cn.edu.huel.user.domain;

import lombok.Data;

/**
 * 营业部表
 *
 * @author 张晓华
 * @date 2023-3-1
 */
@Data
public class BusinessDepartment {

	private Integer id;

	/* 营业网点编号 */
	private String code;

	/* 网点的唯一识别号 */
	private String unifiedCode;

	/* 网点名 */
	private String name;

	/* 详细地址 */
	private String address;

	/* 精度  */
	private String longitude;

	/*  纬度 */
	private String latitude;

	/* 省份代码 */
	private String provinceCode;

	/* 省份名字 */
	private String provinceName;

	/* 城市代码 */
	private String cityCode;

	/* 城市名字 */
	private String cityName;

	/*  区县代码 */
	private String countyCode;

	/* 区县名字 */
	private String countyName;

	/* 业务范围 */
	private String businessType;

	/* 联系电话 */
	private String contactway;

	/* 营业部经纬度 */
	private String amapLongitude;

	/* 营业部经纬度 */
	private String amapLatitude;


}
