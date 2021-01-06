package com.tongtech.service.analysis.impl;


import com.tongtech.dao.entity.analysis.UdaTemplateLevel;
import com.tongtech.dao.repository.analysis.UdaTemplateLevelRepository;
import com.tongtech.service.analysis.IUdaTemplateLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 自定义分析模板粒度表 服务实现类
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
@Service
public class UdaTemplateLevelServiceImpl  implements IUdaTemplateLevelService {
    @Autowired
    private UdaTemplateLevelRepository udaTemplateLevelRepository;

    @Override
    public List<UdaTemplateLevel> findByTemplateId(Integer templateId) {
        return udaTemplateLevelRepository.findByTemplateId(templateId);
    }

    @Override
    public void saveBatch(List<UdaTemplateLevel> udaTemplateLevelList) {
        udaTemplateLevelRepository.saveAll(udaTemplateLevelList);
    }
}
