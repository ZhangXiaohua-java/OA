package cn.edu.huel.user.domain;

import lombok.Data;

/**
 * @author 张晓华
 * @date 2023-3-1
 */
@Data
public class DpParam {

	private String level;

	private String provinceCode;

	private String provinceName;

	private String cityCode;

	private String cityName;

	private String countyCode;

	private String countyName;

	private int pageIndex = 1;

	private int pageSize = 1000;



}
