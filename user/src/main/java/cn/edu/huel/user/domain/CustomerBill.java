package cn.edu.huel.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 顾客账单表
 * @TableName t_customer_bill
 */
@TableName(value ="t_customer_bill")
@Data
public class CustomerBill implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer id;

    /**
     * 消费金额
     */
    private Double amount;

    /**
     * 用户id
     */
    private String customerId;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 账单类型,月度账单还是年度账单,1阅读账单,2年度账单
     */
    private String type;

    /**
     * 账单周期的唯一标识,如年度账单2023,月度账单2023-03
     */
    private String uniqueFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}