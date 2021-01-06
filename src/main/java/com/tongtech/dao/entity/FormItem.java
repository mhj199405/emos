package com.tongtech.dao.entity;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import static javax.persistence.FetchType.LAZY;

/**
 * 表单条目
 *
 * @author 1
 *
 */
@Entity
@Table(name = "frm_form_item")
@Data
public class FormItem  {
    @Id
    private String id;       // 字段ID  由于字段ID由前台生成
    private Long   orders;  // 序号
    private Long   layerId; // 层级ID spdb_form_form_item.id
    private String ename;   // 数据项英文名称
    private String name;    // 项目中文名称
    private String type;   // 前端显示的类型，text dropdown checkbox radio datepicker cascader
    private String icon;

    private Integer isTemplate;      // 是否模板:     0:否； 1:是
    private Integer isHidden;        // 是否隐藏:     0:否； 1:是
    private String  isSearch;        // 是否是检索条件  1：是   0：否
    private Integer isRequired;      // 是否必须字段: 0:否； 1:是
    private Integer isMultText;      // 是否多文本:   0:否； 1:是
    private Integer isDisplay;       //是否显示       0 否   1 是
    private Integer  isPrimaryKey;    //是否是主键     0 否    1 是


    private String  title;            // 标题
    private Integer width;           // 宽度
    private String  parentCtlid;     // 父控件触发ID
    private String  parentCtlvalue;  // 父控件触发值
    private String  value;            // 默认值
    private String  dataType;        // 数据类型 ==文本框扩展中：文本、数字、邮箱，身份证、手机
    @Lob
    @Basic(fetch=LAZY)
    @Column(name="extend",columnDefinition="longtext")
    private String  extend;          // 扩展字段 下拉框restful接口

    private String  scope;           // 共享范围
    private String  unit;            // 单位（查询使用）
    private String minValue;        // 最小值
    private String maxValue;        // 最大值
    private Integer versions;       // s版本
    private String  note;           // 描述

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();//创建时间

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//修改时间

    private Integer  isModify;
    private String  relaItemId;
    @Lob
    @Basic(fetch=LAZY)
    @Column(name="valid_expr",columnDefinition="longtext")
    private String  validExpr;
    @Lob
    @Basic(fetch=LAZY)
    @Column(name="dict_expr",columnDefinition="longtext")
    private String  dictExpr;
    @Lob
    @Basic(fetch=LAZY)
    @Column(name="req_ext",columnDefinition="longtext")
    private String  reqExt;


    @Transient
    private List<FormItemFieldData> dataSourceList = new ArrayList<>();

    public void addFormItemData(FormItemFieldData data){
        if(dataSourceList == null)
            dataSourceList = new ArrayList<>();
        dataSourceList.add(data);
    }
}

