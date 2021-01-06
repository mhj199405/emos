package com.tongtech.dao.entity.analysis;


import com.tongtech.dao.pk.analysis.UdaTemplateLevelSinglePK;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *  template_id   int      comment '模板ID',
 *         dim_id         varchar(255) not null comment '维度编码',
 *         level_id       varchar(255) not null comment '粒度编码',
 *         level_name     varchar(255) not null comment '粒度名称',
 *         single_id      varchar(128) not null comment '单个single的id',
 *         single_value varchar(128) not null comment '单个single的值'
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name="uda_template_level_single")
@IdClass(UdaTemplateLevelSinglePK.class)
public class UdaTemplateLevelSingle implements Serializable {
    @Id
    private Integer templateId;

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

    @Id
    private String singleValue;

}
