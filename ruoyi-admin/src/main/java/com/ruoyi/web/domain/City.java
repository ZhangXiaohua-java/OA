package com.ruoyi.web.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 张晓华
 * @date 2023-3-4
 */
@EqualsAndHashCode(exclude = {"counties"})
@Data
public class City {

	/* 城市代码 */
	private String adcode;

	/* 城市名 */
	private String city;

	/* 城市区号 */
	private String citycode;


	/* 经过的区县列表 */
	private List<County> counties;


}
