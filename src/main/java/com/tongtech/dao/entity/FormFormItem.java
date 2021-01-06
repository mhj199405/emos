package com.tongtech.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Form与FormItem的关联表
 * 父FormItem与子FormItem的关联表
 * @author 1
 *
 */
@Entity
@Table(name = "frm_form_form_item")
@Data
public class FormFormItem extends BaseEntity {
    private Long orders;            //在表单中的序号
    private Long formId;            //表单ID ，模板时为-1
    private String name;            // 层级名称
    private String label;           // 层级显示名称
    private Long parentId;         // 父项目ID -1
    private Integer isList;        // 是否是目录: 0:否； 1: 是
    private Integer isRepeatable; // 是否可多次填写本层下的字段值:0:否；1:是,不固定数量；2:是,固定数量
    private String repeatTitles;  // is_repeatable=2时，实例填写时的标题：序号:标题|；例如：1:堵塞|2:堵塞>75%|3:堵塞>50%
    private String description;
    private Integer isTemplate; // 是否模板: 0:否； 1:是
    private String extType;
    private String extUrl;
    private String extReq;
    private String extBtnId;

    @Transient
    private List<FormItem> formItemList;
    public void addFormItem(FormItem item){
        if(formItemList == null)
            formItemList = new ArrayList<>();
        formItemList.add(item);
    }
}
