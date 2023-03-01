package cn.edu.huel.user.vo;

import lombok.Data;

/**
 * @author 张晓华
 * @date 2023-2-28
 */
@Data
public class PageVo {

	private Integer pageNum;

	private Integer pageSize;


	public Integer getPageNum() {
		return pageNum == null ? 1 : pageNum;
	}

	public Integer getPageSize() {
		return pageSize == null ? 10 : pageSize;
	}


}
