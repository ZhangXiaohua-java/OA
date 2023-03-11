package com.ruoyi.web.vo;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 张晓华
 * @date 2023-3-3
 */
@Data
public class ConditionVo {


	private Integer pageNum = 1;


	private Integer pageSize = 10;


	private String keyword;

	private String[] range;


	private String id;


	private Integer status;


	private Date[] dateRange;

	public Date[] getDateRange() {
		if (Objects.isNull(this.range)) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		this.dateRange = Arrays.stream(this.range).map(e -> {
			try {
				return dateFormat.parse(e);
			} catch (ParseException ex) {
				throw new RuntimeException(ex);
			}
		}).collect(Collectors.toList()).toArray(new Date[]{});
		return dateRange;
	}


}
