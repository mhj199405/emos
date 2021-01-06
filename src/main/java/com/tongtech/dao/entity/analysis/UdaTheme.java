package com.tongtech.dao.entity.analysis;

import com.tongtech.dao.pk.analysis.UdaThemePK;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 自定义分析专题表
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name="uda_theme")
@IdClass(UdaThemePK.class)
public class UdaTheme implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 专题数据集id
     */

    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer themeId;

    /**
     * 版本
     */
    @Id
    private Integer versionId;

    /**
     * 专题数据集名称
     */
    private String themeName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 备注
     */
    private String notes;


}
