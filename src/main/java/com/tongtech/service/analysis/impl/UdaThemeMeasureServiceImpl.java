package com.tongtech.service.analysis.impl;


import com.tongtech.common.utils.SysUtils;
import com.tongtech.common.vo.analysis.MeasureVo;
import com.tongtech.common.vo.analysis.ThemeVo;
import com.tongtech.dao.entity.analysis.UdaThemeMeasure;
import com.tongtech.dao.repository.analysis.UdaThemeMeasureRepository;
import com.tongtech.service.analysis.IUdaThemeMeasureService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * <p>
 * 自定义分析专题维度指标表 服务实现类
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
@Service
public class UdaThemeMeasureServiceImpl implements IUdaThemeMeasureService {

    @Autowired
    UdaThemeMeasureRepository udaThemeMeasureRepository;

    @Override
    public List<UdaThemeMeasure> findAllUdaThemeMeasure() {
        return udaThemeMeasureRepository.findAll();
    }

    @Override
    public void buildMeasure(List<ThemeVo> result) {
        List<UdaThemeMeasure> lst = findAllUdaThemeMeasure();
        if (lst == null || lst.isEmpty()) {
            return;
        }
        MeasureVo voObj;
        for(ThemeVo theme: result) {
            for(UdaThemeMeasure obj:lst) {
                if (!theme.getThemeId().equals(obj.getThemeId().toString())) {
                    continue;
                }
                voObj = new MeasureVo();
                BeanUtils.copyProperties(obj,voObj, SysUtils.getNullPropertyNames(obj));
                theme.getMeasureList().add(voObj);
            }
        }
    }

    @Override
    public UdaThemeMeasure findUdaThemeMeasureByMeasureVo(MeasureVo measureVo, String themeId) {
        return udaThemeMeasureRepository.findOne(new Specification<UdaThemeMeasure>() {
            @Override
            public Predicate toPredicate(Root<UdaThemeMeasure> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.and(cb.equal(root.get("themeId"), themeId), cb.equal(root.get("measureId"), measureVo.getMeasureId()));
            }
        }).get();
    }

    @Override
    public UdaThemeMeasure findUdaThemeMeasureByFiledName(String fieldName, String themeId) {
        return udaThemeMeasureRepository.findOne(new Specification<UdaThemeMeasure>() {
            @Override
            public Predicate toPredicate(Root<UdaThemeMeasure> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.and(cb.equal(root.get("themeId"), themeId), cb.equal(root.get("measureName"),fieldName));
            }
        }).get();
    }
}
