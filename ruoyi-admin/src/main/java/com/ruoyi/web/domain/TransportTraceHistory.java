package com.ruoyi.web.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 运输计划的追踪信息表
 * @TableName t_transport_trace_history
 */
@TableName(value ="t_transport_trace_history")
@Data
public class TransportTraceHistory implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 运输计划的id
     */
    private Integer planId;

    /**
     * 当前的转运场id
     */
    private Integer transferId;

    /**
     * 到达时间
     */
    private Date reachTime;

    /**
     * 离开时间
     */
    private Date leaveTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}