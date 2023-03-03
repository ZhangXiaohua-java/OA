/**
 * Copyright 2023 json.cn
 */
package cn.edu.huel.user.domain;

import java.util.List;

/**
 * Auto-generated: 2023-03-03 14:23:22
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class Cities {

	private String adcode;
	private String citycode;
	private String city;
	private List<Districts> districts;

	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}

	public String getAdcode() {
		return adcode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setDistricts(List<Districts> districts) {
		this.districts = districts;
	}

	public List<Districts> getDistricts() {
		return districts;
	}

}