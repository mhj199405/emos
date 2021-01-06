package com.tongtech.dao.pk.analysis;

import lombok.Data;

import java.io.Serializable;

@Data
public class UdaTemplateMeasurePK implements Serializable {

    private Integer templateId;

    private String measureId;
}
