package com.tongtech.service.analysis.impl;


import com.tongtech.dao.entity.analysis.UdaTemplateMeasure;
import com.tongtech.dao.repository.analysis.UdaTemplateMeasureRepository;
import com.tongtech.service.analysis.IUdaTemplateMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 自定义分析模板指标表 服务实现类
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
@Service
public class UdaTemplateMeasureServiceImpl  implements IUdaTemplateMeasureService {

    @Autowired
    private UdaTemplateMeasureRepository udaTemplateMeasureRepository;

    @Override
    public List<UdaTemplateMeasure> findByTemplateId(Integer templateId) {
        return udaTemplateMeasureRepository.findByTemplateId(templateId);
    }

    @Override
    public void saveBatch(List<UdaTemplateMeasure> udaTemplateMeasureList) {
        udaTemplateMeasureRepository.saveAll(udaTemplateMeasureList);
    }
}
