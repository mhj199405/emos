package com.tongtech.dao.entity.analysis;


import com.tongtech.dao.pk.analysis.UdaTemplateMeasurePK;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * 自定义分析模板指标表
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name="uda_template_measure")
@IdClass(UdaTemplateMeasurePK.class)
public class UdaTemplateMeasure implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     */

    @Id
    private Integer templateId;

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
     * 备注
     */
    private String notes;


}
