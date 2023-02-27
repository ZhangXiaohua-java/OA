package cn.edu.huel.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author 张晓华
 * @date 2023-2-26
 */
@Data
public class AreaVo {

	private Long value;

	private String label;

	private List<AreaVo> children;


	public AreaVo(Long value, String label) {
		this.value = value;
		this.label = label;
	}
}
