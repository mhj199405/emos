package com.tongtech.service.analysis.impl;


import com.tongtech.common.vo.analysis.LevelVo;
import com.tongtech.common.vo.analysis.SingleVo;
import com.tongtech.dao.entity.analysis.UdaThemeDimLevel;
import com.tongtech.dao.entity.analysis.UdaThemeDimLevelSingle;
import com.tongtech.dao.repository.analysis.UdaThemeDimLevelSingleRepository;
import com.tongtech.service.analysis.IUdaThemeDimLevelSingleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class UdaThemeDimLevelSingleServiceImpl  implements IUdaThemeDimLevelSingleService {

    @Autowired
    UdaThemeDimLevelSingleRepository udaThemeDimLevelSingleRepository;

    @Override
    public List<UdaThemeDimLevelSingle> findAllUdaThemeDimLevelSingle(UdaThemeDimLevel level) {
        return udaThemeDimLevelSingleRepository.findAll(new Specification<UdaThemeDimLevelSingle>() {
            @Override
            public Predicate toPredicate(Root<UdaThemeDimLevelSingle> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.and(cb.equal(root.get("dimId"), level.getDimId()), cb.equal(root.get("themeId"), level.getThemeId()), cb.equal(root.get("levelId"), level.getLevelId()));
            }
        });
    }

    @Override
    public void buildSingle(LevelVo level1, UdaThemeDimLevel level) {
        List<UdaThemeDimLevelSingle> list = findAllUdaThemeDimLevelSingle(level);
        if (list == null || list.size()==0){
            level1.setValList(new ArrayList<>());
            return;
        }
        SingleVo singleVo;
        for (UdaThemeDimLevelSingle udaThemeDimLevelSingle : list) {
            singleVo=new SingleVo();
            singleVo.setId(udaThemeDimLevelSingle.getSingleId());
            singleVo.setValue(udaThemeDimLevelSingle.getSingleValue());
            level1.getValList().add(singleVo);
        }
    }

    @Override
    public Page<UdaThemeDimLevelSingle> page(Pageable pageable) {
        return udaThemeDimLevelSingleRepository.findAll(pageable);
    }
}
