package cn.edu.huel.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 配送范围支持表
 * @TableName t_service_support_range
 */
@TableName(value ="t_service_support_range")
@Data
public class ServiceSupportRange implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 街道名字
     */
    private String streetName;

    /**
     * 全国街道统一编码
     */
    private String streetCode;

    /**
     * 是否支持上门服务,N不支持,Y支持
     */
    private String intoWareHouse;

    /**
     * 乡村名字,暂时不考虑设计
     */
    private String townname;

    /**
     * 所属区县的城市代码
     */
    private String countCode;

    /**
     * 所属区县的名字
     */
    private String countName;

    /**
     * 配送方式,1全境派送,2乡镇自提,3镇中心派送
     */
    private String rangeTypeCode;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}