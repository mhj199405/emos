package com.tongtech.dao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonKeyDictTPK implements Serializable {
    private String busiType;
    private Integer keyId ;
}
