package com.tongtech.dao.entity;

import com.tongtech.dao.pk.DepartmentFormPk;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "frm_department_form")
@IdClass(DepartmentFormPk.class)
@Data
public class DepartmentForm {
    @Id
    private Integer formId;
    @Id
    private Integer roleId;
    @Id
    private Integer userId;
}
