package com.tongtech.auth.data.db_sys_data_control;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 *  id                int NOT NULL AUTO_INCREMENT,
 *   data_name         varchar(255)   DEFAULT NULL COMMENT '数据名称',
 *   data_param        varchar(100)   NOT NULL COMMENT '字段属性',
 *   data_desc         varchar(1000)   DEFAULT NULL COMMENT '描述',
 *   data_status       int DEFAULT NULL COMMENT '状态',
 *   data_operation    int NOT NULL COMMENT '类型，0；等于，1：大于，2：小于，3：大于等于，4：小于等于，5：介于之间',
 *   data_value1       bigint DEFAULT NULL COMMENT '最小值',
 *   data_value2       bigint DEFAULT NULL COMMENT '最大值',
 *   control_order     int NOT NULL DEFAULT '0' COMMENT '排序字段',
 *   create_time       datetime NOT NULL,
 */
@Entity
@Data
@Table(name = "sys_data_control")
public class SysDataControl {

    @Id
//    @SequenceGenerator(name = "control_seq",sequenceName = "control_seq",allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "control_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    private String  dataName;//数据名称

    private String  dataParam;//字段属性

    private String  dataDesc;//描述

    private Integer dataStatus;//状态

    private Integer dataOperation;//类型

    private Integer controlOrder;//排序字段

    private Long dataValue1;//最小值

    private Long dataValue2;//最大值

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JSONField(format ="yyyy-MM-dd HH:mm:ss" )
    private Date createTime; //创建时间

}
