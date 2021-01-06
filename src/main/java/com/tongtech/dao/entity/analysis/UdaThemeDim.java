package com.tongtech.dao.entity.analysis;


import com.tongtech.dao.pk.analysis.UdaThemeDimPK;
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
 * 自定义分析专题维度维度表
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name="uda_theme_dim")
@IdClass(UdaThemeDimPK.class)
public class UdaThemeDim implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 专题数据集id
     */
    @Id
  //  @Column(name = "theme_id")
    private Integer themeId;

    /**
     * 版本
     */
    @Id
   // @Column(name = "version_id")
    private Integer versionId;

    /**
     * 指标/维度id
     */
    @Id
   // @Column(name = "dim_id")
    private String dimId;

    /**
     * 指标名称
     */
    private String dimName;

    /**
     * 备注
     */
    private String notes;


}
