package com.tongtech.dao.entity.analysis;


import com.tongtech.dao.pk.analysis.UdaThemeMeasurePK;
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
 * 自定义分析专题维度指标表
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name="uda_theme_measure")
@IdClass(UdaThemeMeasurePK.class)
public class UdaThemeMeasure implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 专题数据集id
     */
    @Id
    private Integer themeId;

    /**
     * 版本
     */
    @Id
    private Integer versionId;

    /**
     * 指标id
     */
    @Id
    private String measureId;

    /**
     * 指标名称
     */
    private String measureName;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 备注
     */
    private String notes;


}
