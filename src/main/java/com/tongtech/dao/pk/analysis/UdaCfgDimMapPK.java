package com.tongtech.dao.pk.analysis;


import lombok.Data;

import java.io.Serializable;


@Data
public class UdaCfgDimMapPK implements Serializable {
    private Integer themeId;

    private String timeLevelId;

    private String geoLevelId;

    private String propLevelId;

    private String propLevelVal;
}
