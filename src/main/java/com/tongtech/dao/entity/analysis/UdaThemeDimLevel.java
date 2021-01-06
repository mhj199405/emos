package com.tongtech.dao.entity.analysis;

import com.tongtech.dao.pk.analysis.UdaThemeDimLevelPK;
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
 * 自定义分析专题维度值信息
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name="uda_theme_dim_level")
@IdClass(UdaThemeDimLevelPK.class)
public class UdaThemeDimLevel implements Serializable {

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
     * 维度编码
     */
    @Id
    private String dimId;

    /**
     * 维度值编码
     */
    @Id
    private String levelId;

    /**
     * 维度值名称
     */
    private String levelName;

    private String fieldName;


}
