package com.tongtech.dao.entity.analysis;


import com.tongtech.dao.pk.analysis.UdaThemeDimLevelSinglePK;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *   theme_id   int      comment '模板ID',
 *         version_id     int          not null comment '版本',
 *         dim_id         varchar(255) not null comment '维度编码',
 *         level_id       varchar(255) not null comment '粒度编码',
 *         level_name     varchar(255) not null comment '粒度名称',
 *         single_id      varchar(128) not null comment '单个single的id',
 *         single_value varchar(128) not null comment '单个single的值',
 *         primary key (theme_id, version_id, dim_id, level_id, single_id)
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name="uda_theme_dim_level_single")
@IdClass(UdaThemeDimLevelSinglePK.class)
public class UdaThemeDimLevelSingle implements Serializable {
    @Id
    private Integer themeId;

    private Integer versionId;

    /**
     * 维度编码
     */
    @Id
    private String dimId;

    /**
     * 粒度编码
     */
    @Id
    private String levelId;

    /**
     * 粒度名称
     */
    private String levelName;

    @Id
    private String singleId;


    private String singleValue;

}
