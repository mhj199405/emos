package com.tongtech.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "frm_template_dataitem")
@Data
public class TemplateDataItem {
    @Id
    private String id               ;   // 数据项ID
    private String name             ;  // 数据项英文名称
    private String title            ;  // 数据项中文名称
    private Integer classId            ; // 数据类别ID
    private String type             ;   // 类型 == text dropdown checkbox radio datepicker cascader
    private String icon             ; // 图标
    private Integer isMultText   ;  // 是否多文本:   0:否； 1:是
    private String value            ;  // 默认值
    private String dataType       ; // 数据类型 ==文本框扩展中：文本、数字、邮箱，身份证、手机
    @Lob
    @Basic(fetch=LAZY)
    @Column(name="extend",columnDefinition="text")
    private String  extend         ;  // 扩展字段 下拉框restful接口

    private String unit             ;  // 单位（查询使用）
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();//创建时间

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//修改时间
    private String note             ;  // 注释
}
