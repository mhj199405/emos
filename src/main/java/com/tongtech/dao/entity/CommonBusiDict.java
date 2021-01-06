package com.tongtech.dao.entity;

import com.tongtech.dao.pk.CommonBusiDictPk;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="common_busi_dict")
@IdClass(CommonBusiDictPk.class)
@Data
public class CommonBusiDict {
    @Id
    private String busiType;
    @Id
    private String busiCode;

    private String busiTypeName;

    private String busiName;

    private String remark;

}
