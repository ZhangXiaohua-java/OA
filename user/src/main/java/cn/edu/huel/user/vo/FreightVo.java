package cn.edu.huel.user.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author 张晓华
 * @date 2023-3-4
 */
@Data
public class FreightVo {

	@NotNull(message = "重量不可为空", groups = {CountCostWithOrderId.class, CountCostWithoutOrderId.class})
	private Integer weight;


	@NotNull(message = "体积不可为空", groups = {CountCostWithOrderId.class, CountCostWithoutOrderId.class})
	private Integer volume;

	@NotNull(message = "订单号不可为空", groups = {CountCostWithOrderId.class})
	@Pattern(regexp = "^\\d{10,}$", message = "非法的订单号", groups = {CountCostWithOrderId.class})
	private String orderId;


	/* 始发地邮编 */
	@NotNull(message = "出发地不可为空", groups = {CountCostWithoutOrderId.class})
	@Length(min = 6, max = 6, message = "非法的城市代码", groups = {CountCostWithoutOrderId.class})
	private String origin;


	/* 目的地邮编 */
	@NotNull(message = "目的地不允许为空", groups = {CountCostWithoutOrderId.class})
	@Length(min = 6, max = 6, message = "非法的城市代码", groups = {CountCostWithoutOrderId.class})
	private String dest;


	public static interface CountCostWithOrderId {
	}

	public static interface CountCostWithoutOrderId {
	}


}
