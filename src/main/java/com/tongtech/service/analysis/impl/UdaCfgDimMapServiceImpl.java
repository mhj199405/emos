package com.tongtech.service.analysis.impl;


import com.tongtech.dao.entity.analysis.UdaCfgDimMap;
import com.tongtech.dao.repository.analysis.UdaCfgDimMapRepository;
import com.tongtech.service.analysis.IUdaCfgDimMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * <p>
 * 自定义分析专题事实表匹配规则表 服务实现类
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
@Service
public class UdaCfgDimMapServiceImpl implements IUdaCfgDimMapService {

    @Autowired
    UdaCfgDimMapRepository udaCfgDimMapRepository;

    @Override
    public UdaCfgDimMap findUdaCfgDimMap(String themeId, String timeDimLevelId, String geoDimLevelId) {
        return udaCfgDimMapRepository.findOne(new Specification<UdaCfgDimMap>() {
            @Override
            public Predicate toPredicate(Root<UdaCfgDimMap> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.and(cb.equal(root.get("themeId"), themeId), cb.equal(root.get("timeLevelId"),timeDimLevelId ),cb.equal(root.get("geoLevelId"),geoDimLevelId));
            }
        }).get();
    }
}
