package com.tongtech.dao.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;


import com.tongtech.common.enums.FormScope;
import lombok.Data;

@Entity
@Table(name = "frm_form")
@Data
public class Form extends BaseEntity{
    private String name;
    private Integer versions;     //表单的版本
    private String description;
    private String status;       // 状态：0：禁用，1：启用
    private Integer isTemplate; // 是否模板: 0:否； 1:是
    private Integer isHideFfi;  // 是否隐藏层级节点：0：否；1：是

    @Enumerated(EnumType.STRING)
    private FormScope scope;

    private String formKey;
    @Transient
    private List<FormFormItem> formFormItemList;
    public void addFormFormItem(FormFormItem formFormItem){
        if(formFormItemList == null)
            formFormItemList = new ArrayList<>();
        formFormItemList.add(formFormItem);
    }

}
