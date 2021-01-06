package com.tongtech.service.analysis.impl;


import com.tongtech.dao.entity.analysis.UdaTemplateLevelSingle;
import com.tongtech.dao.repository.analysis.UdaTemplateLevelSingleRepository;
import com.tongtech.service.analysis.IUdaTemplateLevelSingleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UdaTemplateLevelSingleServiceImpl  implements IUdaTemplateLevelSingleService {
    @Autowired
    private UdaTemplateLevelSingleRepository udaTemplateLevelSingleRepository;
    @Override
    public List<UdaTemplateLevelSingle> findByDimIdAndGetLevelIdAndTemplatedId(String dimId, String levelId, Integer templateId) {
        return udaTemplateLevelSingleRepository.findByDimIdAndLevelIdAndTemplateId(dimId,levelId,templateId);
    }

    @Override
    public void saveBatch(List<UdaTemplateLevelSingle> udaTemplateLevelSingleList) {
        udaTemplateLevelSingleRepository.saveAll(udaTemplateLevelSingleList);
    }
}
