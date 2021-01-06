package com.tongtech.service.analysis;

import com.tongtech.dao.entity.analysis.UdaTemplateLevel;

import java.util.List;

/**
 * <p>
 * 自定义分析模板粒度表 服务类
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
public interface IUdaTemplateLevelService  {

    /**
     * 根据TemplateId查找UdaTemplateLevel
     * @param templateId
     * @return 返回一个UdaTemplateLevel列表
     */
    List<UdaTemplateLevel> findByTemplateId(Integer templateId);

    /**
     * 保存udaTemplateLevel
     * @param udaTemplateLevelList
     */
    void saveBatch(List<UdaTemplateLevel> udaTemplateLevelList);
}
