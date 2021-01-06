package com.tongtech.dao.entity.analysis;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * 自定义分析模板表
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name="uda_template")
public class UdaTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 专题数据集id
     */
    private Integer themeId;

    /**
     * 是否公开
     */
    private Integer hasShare;

    /**
     * 条件
     */
    private String conditions;


}
