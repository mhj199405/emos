package com.tongtech.dao.entity;

import com.tongtech.dao.pk.TemplateDataItemDataPK;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "frm_template_dataitem_data")
@IdClass(TemplateDataItemDataPK.class)
@Data
public class TemplateDataItemData {
    @Id
    private String dataitemId;   // 表单字段ID
    @Id
    private String id;        // 数据Id
    private Integer   orders;  // 序号
    private String name;      // 数据名称
    private String parentId; // 父项目ID
}