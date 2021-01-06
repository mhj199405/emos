package com.tongtech.dao.pk;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

@Data
public class DepartmentFormPk implements Serializable {

    private Integer formId;

    private Integer roleId;

    private Integer userId;
}
