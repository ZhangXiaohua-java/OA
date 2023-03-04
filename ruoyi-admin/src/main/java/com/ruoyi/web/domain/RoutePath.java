package com.ruoyi.web.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 张晓华
 * @date 2023-3-4
 * 规划路径
 */
@Data
public class RoutePath {


	/* 路程距离,单位米 */
	private Long distance;

	/* 收费站收费,单位元 */
	private BigDecimal tolls;

	/* 收费路段里程,单位米 */
	private Long tollDistance;

	/* 路线中红绿灯个数 */
	private Long trafficLights;

	/* 当前方案经过的城市列表 */
	private List<City> cities;




}
