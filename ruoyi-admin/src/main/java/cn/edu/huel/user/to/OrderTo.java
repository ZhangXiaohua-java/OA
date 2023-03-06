package cn.edu.huel.user.to;

import lombok.Data;

/**
 * @author 张晓华
 * @date 2023-3-1
 */
@Data
public class OrderTo {

	private String orderId;

	private Integer weight;

	private Integer volume;

	private Integer code;

	private String employee;


}
