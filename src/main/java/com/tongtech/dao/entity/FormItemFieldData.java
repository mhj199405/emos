package com.tongtech.dao.entity;


import com.tongtech.dao.pk.FormItemFieldDataPK;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "frm_form_item_data")
@IdClass(FormItemFieldDataPK.class)
@Data
public class FormItemFieldData {
    @Id
    private String fieldId;   // 表单字段ID
    @Id
    private String id;        // 数据Id
    private Long   orders;  // 序号
    private String name;      // 数据名称
    private String parentId; // 父项目ID
}

