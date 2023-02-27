package cn.edu.huel.user.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author 张晓华
 * @date 2023-2-27
 */
@Data
public class ConditionVo {

	public enum OrderStatus {

		/**
		 * {key: 1, label: "已完成"},
		 * {key: 2, label: "运输中"},
		 * {key: 3, label: "未支付"},
		 */
		FINISHED(1, "已完成"),
		TRANSPORT(2, "已完成"),
		NOT_PAY(3, "未支付");

		private int code;

		private String label;

		OrderStatus(int code, String label) {
			this.code = code;
			this.label = label;
		}

		public int getCode() {
			return code;
		}

		public String getLabel() {
			return label;
		}
	}

	/* 订单的日期区间 */
	private Date[] range;

	/* 订单号 */
	private String orderId;

	/* 选中的订单状态 */
	private String checkItems;

	/* 顾客id */
	private String customerId;

	private Integer pageNum = 1;

	private Integer pageSize = 10;

}
