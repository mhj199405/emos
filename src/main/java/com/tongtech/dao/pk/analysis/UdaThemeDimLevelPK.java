package com.tongtech.dao.pk.analysis;

import lombok.Data;

import java.io.Serializable;

@Data
public class UdaThemeDimLevelPK implements Serializable {
    private Integer themeId;

    private Integer versionId;

    private String dimId;

    private String levelId;
}
