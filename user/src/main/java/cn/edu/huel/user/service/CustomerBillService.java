package cn.edu.huel.user.service;

import cn.edu.huel.user.domain.CustomerBill;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * @author ZhangXiaoHua
 * @description 针对表【t_customer_bill(顾客账单表)】的数据库操作Service
 * @createDate 2023-03-06 11:22:18
 */
public interface CustomerBillService extends IService<CustomerBill> {


	/*  年度账单 */
	String YEAR_TERM_BILL = "1";


	/*  月度账单 */
	String MONTH_TERM_BILL = "2";


	/**
	 * @return 用户的年度账单
	 */
	CustomerBill queryCustomerYearBill();

	/**
	 * @param customerId 顾客id
	 * @return 用户的年度账单
	 */
	CustomerBill queryCustomerYearBill(String customerId);

	/**
	 * @param customerId 顾客id
	 * @return 月度账单
	 */
	CustomerBill queryCustomerMonthBill(String customerId);


	/**
	 * @return 查询该顾客待支付金额
	 */
	BigDecimal queryAllUnpaiedAmount(String customerId);


	/**
	 * @param customerId 顾客id
	 * @param billType   账单类型
	 * @param uniqueFlag 唯一标识
	 * @return 账单
	 */
	CustomerBill queryBill(String customerId, String billType, String uniqueFlag);


	/**
	 * @param customerId 顾客id
	 * @param amount     金额
	 * @return 增加记录
	 */
	boolean updateAmount(String customerId, String amount);


	/**
	 * @param billId     账单id
	 * @param amount     金额
	 * @param billType   账单类型
	 * @param uniqueFlag 唯一标识
	 * @param customerId 顾客id
	 * @return 更新账单
	 */
	boolean updateBill(Integer billId, BigDecimal amount, String billType, String uniqueFlag, String customerId);


}
