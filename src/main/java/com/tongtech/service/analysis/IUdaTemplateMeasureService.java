package com.tongtech.service.analysis;

import com.tongtech.dao.entity.analysis.UdaTemplateMeasure;

import java.util.List;

/**
 * <p>
 * 自定义分析模板指标表 服务类
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
public interface IUdaTemplateMeasureService  {
    /**
     * 查询UdaTemplateMeasure
     * @param templateId
     * @return UdaTemplateMeasure列表
     */
    List<UdaTemplateMeasure> findByTemplateId(Integer templateId);

    /**
     * 批量保存指标UdaTemplateMeasure
     * @param udaTemplateMeasureList
     */
    void saveBatch(List<UdaTemplateMeasure> udaTemplateMeasureList);
}
