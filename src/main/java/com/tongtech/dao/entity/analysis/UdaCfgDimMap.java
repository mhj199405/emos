package com.tongtech.dao.entity.analysis;


import com.tongtech.dao.pk.analysis.UdaCfgDimMapPK;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * <p>
 * 自定义分析专题事实表匹配规则表
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name="uda_cfg_dim_map")
@IdClass(UdaCfgDimMapPK.class)
public class UdaCfgDimMap implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 专题数据集id
     */
    @Id
    private Integer themeId;

    /**
     * 时间粒度编码
     */
    @Id
    private String timeLevelId;

    /**
     * 时间名称
     */
    private String timeLevelName;

    /**
     * 地理粒度编码
     */
    @Id
    private String geoLevelId;

    /**
     * 地理粒度名称
     */
    private String geoLevelName;

    /**
     * 属性编码
     */
    @Id
    private String propLevelId;

    /**
     * 属性名称
     */
    private String propLevelName;

    /**
     * 属性值,多个值,分割
     */
    @Id
    private String propLevelVal;

    /**
     * 对于表名称，需要替换部分使用$$xx$$
     */
    private String tableName;


}
