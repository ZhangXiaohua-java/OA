package com.ruoyi.web.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 张晓华
 * @date 2023-3-8
 */
@AllArgsConstructor
@Data
public class PageVo {

	/* 当前页码 */
	private Long current;

	/* 每页条目数 */
	private Long size;

	/* 总条目数 */
	private Long total;

	/* 总页数*/
	private Long totalPages;


	public static PageVo getPage(Page page) {
		return new PageVo(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages());
	}




}
