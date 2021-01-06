package com.tongtech.dao.pk.analysis;

import lombok.Data;

import java.io.Serializable;

@Data
public class UdaTemplateLevelSinglePK implements Serializable {
    private Integer templateId;

    private String dimId;

    private String levelId;

    private String singleId;

    private String singleValue;
}
