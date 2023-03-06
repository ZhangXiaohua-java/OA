package cn.edu.huel.user.service.impl;

import cn.edu.huel.user.domain.CustomerBill;
import cn.edu.huel.user.mapper.CustomerBillMapper;
import cn.edu.huel.user.mapper.PostOrderMapper;
import cn.edu.huel.user.service.CustomerBillService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/**
 * @author ZhangXiaoHua
 * @description 针对表【t_customer_bill(顾客账单表)】的数据库操作Service实现
 * @createDate 2023-03-06 11:22:18
 */
@Service
public class CustomerBillServiceImpl extends ServiceImpl<CustomerBillMapper, CustomerBill>
		implements CustomerBillService {


	private static DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");

	private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

	@Resource
	private PostOrderMapper orderMapper;


	/**
	 * @return 用户的年度账单
	 */
	@Override
	public CustomerBill queryCustomerYearBill() {
		String year = yearFormatter.format(LocalDate.now());
		String customerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		CustomerBillService proxy = (CustomerBillService) AopContext.currentProxy();
		return proxy.queryBill(customerId, CustomerBillService.YEAR_TERM_BILL, year);
	}


	/**
	 * @param customerId
	 * @return 查询该顾客待支付金额
	 */
	@Override
	public BigDecimal queryAllUnpaiedAmount(String customerId) {
		return orderMapper.selectUnpaiedAmount(customerId);
	}


	/**
	 * @param customerId 顾客id
	 * @return 月度账单
	 */
	@Override
	public CustomerBill queryCustomerMonthBill(String customerId) {
		CustomerBillService proxy = (CustomerBillService) AopContext.currentProxy();
		String month = dateFormatter.format(LocalDate.now());
		return proxy.queryBill(customerId, CustomerBillService.MONTH_TERM_BILL, month);
	}


	/**
	 * @param customerId 顾客id
	 * @param billType   账单类型
	 * @param uniqueFlag 唯一标识
	 * @return 账单
	 */
	@Override
	public CustomerBill queryBill(String customerId, String billType, String uniqueFlag) {
		LambdaQueryWrapper<CustomerBill> query = new LambdaQueryWrapper<>();
		query.eq(CustomerBill::getCustomerId, customerId)
				.and(c -> c.eq(CustomerBill::getType, billType)
						.eq(CustomerBill::getUniqueFlag, uniqueFlag));
		CustomerBill bill = this.baseMapper.selectOne(query);
		// 查询的当年账单不存在就为用户创建一个
		if (Objects.isNull(bill)) {
			bill = new CustomerBill();
			bill.setCustomerId(customerId);
			bill.setAmount(0.0);
			bill.setType(billType);
			bill.setUniqueFlag(uniqueFlag);
			bill.setUpdateTime(new Date());
			this.baseMapper.insert(bill);
		}
		return bill;
	}


	/**
	 * @param customerId 顾客id
	 * @param amount     金额
	 * @return 增加记录
	 */
	@Override
	@Transactional(rollbackFor = {Throwable.class})
	public boolean updateAmount(String customerId, String amount) {
		CustomerBillService proxy = (CustomerBillService) AopContext.currentProxy();
		CustomerBill bill = proxy.queryCustomerYearBill(customerId);
		String year = yearFormatter.format(LocalDate.now());
		String month = dateFormatter.format(LocalDate.now());
		boolean result = false;
		result = proxy.updateBill(bill.getId(), new BigDecimal(amount), CustomerBillService.YEAR_TERM_BILL, year, customerId);
		if (!result) {
			throw new UnsupportedOperationException("账单更新失败");
		}
		bill = proxy.queryCustomerMonthBill(customerId);
		result = proxy.updateBill(bill.getId(), new BigDecimal(amount), CustomerBillService.MONTH_TERM_BILL, month, customerId);
		if (!result) {
			throw new UnsupportedOperationException("账单更新失败");
		}
		return result;
	}


	/**
	 * @param customerId 顾客id
	 * @return 用户的年度账单
	 */
	@Override
	public CustomerBill queryCustomerYearBill(String customerId) {
		CustomerBillService proxy = (CustomerBillService) AopContext.currentProxy();
		String year = yearFormatter.format(LocalDate.now());
		return proxy.queryBill(customerId, CustomerBillService.YEAR_TERM_BILL, year);
	}


	/**
	 * @param billId     账单id
	 * @param amount     金额
	 * @param billType   账单类型
	 * @param uniqueFlag 唯一标识
	 * @param customerId 顾客id
	 * @return 更新账单
	 */
	@Override
	public boolean updateBill(Integer billId, BigDecimal amount, String billType, String uniqueFlag, String customerId) {
		CustomerBillService proxy = (CustomerBillService) AopContext.currentProxy();
		CustomerBill bill = proxy.queryBill(customerId, billType, uniqueFlag);
		CustomerBill update = new CustomerBill();
		update.setId(bill.getId());
		update.setUpdateTime(new Date());
		double current = BigDecimal.valueOf(bill.getAmount())
				.add(amount).doubleValue();
		update.setAmount(current);
		return proxy.updateById(update);
	}


}




