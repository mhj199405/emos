package com.tongtech.dao.pk;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonBusiDictPk implements Serializable {

    private String busiType;

    private String busiCode;
}
