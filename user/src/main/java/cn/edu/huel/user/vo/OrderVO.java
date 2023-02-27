package cn.edu.huel.user.vo;

import cn.edu.huel.user.base.standard.CreateOrder;
import cn.edu.huel.user.base.validator.PayTypeCheck;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * @author 张晓华
 * @date 2023-2-23
 */
@Data
public class OrderVO {


	@NotBlank(message = "寄件人姓名不可为空", groups = {CreateOrder.class})
	private String posterName;

	@NotBlank(message = "寄件人手机号不可为空", groups = {CreateOrder.class})
	private String posterPhone;

	@NotBlank(message = "收件人姓名不可为空", groups = {CreateOrder.class})
	private String receiverName;

	@NotBlank(message = "收件人手机号不可为空", groups = {CreateOrder.class})
	private String receiverPhone;

	/* 寄件地址的城市邮编 */
	@NotBlank(message = "邮寄地址不能为空", groups = {CreateOrder.class})
	private String postCode;

	/* 收件地址的城市邮编 */
	@NotBlank(message = "邮寄地址不能为空", groups = {CreateOrder.class})
	private String receiveCode;

	/* 上门揽件的详细带地址 */
	@NotBlank(message = "收件地址不能为空", groups = {CreateOrder.class})
	private String detailAddress;

	/* 收件的详细地址 */
	@NotBlank(message = "收件地址不能为空", groups = {CreateOrder.class})
	private String dispatchDetailAddress;

	@NotBlank(message = "货物类型不能为空", groups = {CreateOrder.class})
	private String goodsName;

	@NotNull(message = "货物重量不允许为空", groups = {CreateOrder.class})
	@Min(value = 1L, message = "起送重量为1KG", groups = {CreateOrder.class})
	private Long weight;

	private Double volume;

	/* 保价 */
	private Integer insure;

	/* 是否需要签收单 */
	private boolean receipt;

	/* 签收单类型 */
	private String receiptType;

	/* 支付方式 */
	@NotNull(message = "支付方式不可为空", groups = {CreateOrder.class})
	@PayTypeCheck(message = "支付方式不支持", groups = {CreateOrder.class})
	private Integer payType;

	/* 预约的上门揽件时间 */
	@NotNull(message = "请指定上门时间", groups = {CreateOrder.class})
	@Future(message = "无效的上门时间信息", groups = {CreateOrder.class})
	private Date appointmentTime;

	/* 邮寄地址的邮编 */
	@Length(max = 6, min = 6, message = "非法的寄件地址", groups = {CreateOrder.class})
	@Pattern(regexp = "\\d{6}", message = "非法的寄件地址", groups = {CreateOrder.class})
	private String posterZipCode;

	/* 收件地址的邮编 */
	@Length(max = 6, min = 6, message = "非法的收件地址", groups = {CreateOrder.class})
	@Pattern(regexp = "\\d{6}", message = "非法的收件地址", groups = {CreateOrder.class})
	private String receiveZipCode;


	/* 签收口令 */
	private String token;

	/* 订单备注信息 */
	private String remark;


	private String customerId;

	/* 订单号 */
	private String orderId;


}
