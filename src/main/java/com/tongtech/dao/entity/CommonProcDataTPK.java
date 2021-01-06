package com.tongtech.dao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonProcDataTPK  implements Serializable  {
    private Integer insProcId;
    private Integer keyId;
    private Integer keyIns;
}
