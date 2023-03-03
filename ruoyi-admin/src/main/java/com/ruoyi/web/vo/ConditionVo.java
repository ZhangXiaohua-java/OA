package com.ruoyi.web.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author 张晓华
 * @date 2023-3-3
 */
@Data
public class ConditionVo {


	private Integer pageNum = 1;


	private Integer pageSize = 10;


	private Date[] range;


	private String id;


	private Integer status;


}
