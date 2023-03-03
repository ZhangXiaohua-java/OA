package cn.edu.huel.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 张晓华
 * @date 2023-2-26
 */
@Data
@NoArgsConstructor
public class AreaVo {

	/* id */
	private Long value;


	/* 展示的名字*/
	private String label;

	/* tree代表有子节点*/
	private boolean leaf;

	private List<AreaVo> children;


	public AreaVo(Long value, String label) {
		this.value = value;
		this.label = label;
	}
}
