package com.ruoyi.web.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 运输计划详情表
 * @TableName t_transport_plan_detail
 */
@TableName(value ="t_transport_plan_detail")
@Data
public class TransportPlanDetail implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 计划的运输方式
     */
    private String transportType;

    /**
     * 核算成本
     */
    private Long planCost;

    /**
     * 实际的运输成本
     */
    private Long cost;

    /**
     * 负责人id
     */
    private Long chargePerson;

    /**
     * 该运输方式对应的可以运输的货物的体积
     */
    private Long expectCapacity;

    /**
     * 该运输方式对应的可以运输的货物的重量
     */
    private Long expectWeight;

    /**
     * 货物实际占用的体积
     */
    private Long capacity;

    /**
     * 货物实际的重量
     */
    private Long weight;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}