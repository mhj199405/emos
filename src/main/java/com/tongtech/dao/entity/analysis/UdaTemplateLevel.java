package com.tongtech.dao.entity.analysis;


import com.tongtech.dao.pk.analysis.UdaTemplateLevelPK;
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
 * 自定义分析模板粒度表
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name="uda_template_level")
@IdClass(UdaTemplateLevelPK.class)
public class UdaTemplateLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     */

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

    /**
     * 计算方法
     */
    private Integer valType;

//    /**
//     * 计算值，用逗号分隔
//     */
//    private String valList;

    @Id
    private String showOrFilter;

    private Boolean hasDict;
}
