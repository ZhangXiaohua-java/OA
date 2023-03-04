/**
 * Copyright 2023 json.cn
 */
package com.ruoyi.web.domain;

import lombok.Data;

import java.util.List;

/**
 * Auto-generated: 2023-03-03 14:23:22
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
public class Cities {

	private String adcode;

	private String citycode;

	private String city;

	private List<Districts> districts;

}