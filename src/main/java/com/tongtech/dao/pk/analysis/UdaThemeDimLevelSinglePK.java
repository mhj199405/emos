package com.tongtech.dao.pk.analysis;

import lombok.Data;

import java.io.Serializable;

@Data
public class UdaThemeDimLevelSinglePK implements Serializable {
    private Integer themeId;

    private String dimId;

    private String levelId;

    private String singleId;
}
