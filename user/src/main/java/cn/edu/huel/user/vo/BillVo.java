package cn.edu.huel.user.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author 张晓华
 * @date 2023-3-6
 */
@Data
public class BillVo {

	/**
	 * 消费金额
	 */
	private Double amount;


	/**
	 * 账单类型,月度账单还是年度账单,1阅读账单,2年度账单
	 */
	private String type;

	/**
	 * 账单周期的唯一标识,如年度账单2023,月度账单2023-03
	 */
	private String uniqueFlag;


	/* 待支付金额 */
	private String pay;


	/* 会员等级 */
	private Integer level;





}
