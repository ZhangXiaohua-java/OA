package cn.edu.huel.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 支付明细表
 * @TableName t_order_pay_detail
 */
@TableName(value ="t_order_pay_detail")
@Data
public class OrderPayDetail implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 支付流水号
     */
    private String tradeNum;

    /**
     * 支付方式,1 支付宝
     */
    private String type;

    /**
     * 支付账号信息
     */
    private String payAccount;

    /**
     * 支付金额
     */
    private Double amount;

    /**
     * 收款账号
     */
    private String receiveAccount;

    /**
     * 交易时间
     */
    private Date payTime;

    /**
     * 顾客id
     */
    private String customerId;

    /**
     * 交易状态,0失败,1成功
     */
    private Integer status;

    /**
     * 删除标志,0为删除,1已删除
     */
    private Integer delFlag;

    /**
     * 顾客支付时的周期账单id,冗余字段,减少查表次数
     */
    private Integer billId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}