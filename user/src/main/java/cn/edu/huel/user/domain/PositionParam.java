package cn.edu.huel.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 张晓华
 * @date 2023-3-1
 */
@Data
@AllArgsConstructor
public class PositionParam {


	/* 经度 */
	private double longitude;

	/* 纬度 */
	private double latitude;


}
