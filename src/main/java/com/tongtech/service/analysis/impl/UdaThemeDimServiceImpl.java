package com.tongtech.service.analysis.impl;


import com.tongtech.common.utils.SysUtils;
import com.tongtech.common.vo.analysis.DimensionVo;
import com.tongtech.common.vo.analysis.LevelVo;
import com.tongtech.common.vo.analysis.ThemeVo;
import com.tongtech.dao.entity.analysis.UdaThemeDim;
import com.tongtech.dao.repository.analysis.UdaThemeDimRepository;
import com.tongtech.service.analysis.IUdaThemeDimLevelService;
import com.tongtech.service.analysis.IUdaThemeDimService;
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
 * 自定义分析专题维度维度表 服务实现类
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
@Service
public class UdaThemeDimServiceImpl  implements IUdaThemeDimService {

    @Autowired
    UdaThemeDimRepository udaThemeDimRepository;

    @Autowired
    private IUdaThemeDimLevelService themeDimLevelService;

    @Override
    public List<UdaThemeDim> findAllUdaThemeDim() {
        return udaThemeDimRepository.findAll();
    }

    @Override
    public void buildDim(List<ThemeVo> result) {
        List<UdaThemeDim> lst = findAllUdaThemeDim();
        if (lst == null || lst.isEmpty()) {
            return;
        }
        DimensionVo voObj;
        for (ThemeVo theme : result) {
            for (UdaThemeDim obj : lst) {
                if (!theme.getThemeId().equals(obj.getThemeId().toString())) {
                    continue;
                }
                voObj = new DimensionVo();
                BeanUtils.copyProperties(obj, voObj, SysUtils.getNullPropertyNames(obj));
                themeDimLevelService.buildLevel(obj, voObj);
                theme.getDimList().add(voObj);
            }
        }
    }

    @Override
    public UdaThemeDim findUdaThemeDimByLevelVo(LevelVo levelVo, String themeId) {



        return udaThemeDimRepository.findOne(new Specification<UdaThemeDim>() {
            @Override
            public Predicate toPredicate(Root<UdaThemeDim> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.and(cb.equal(root.get("themeId"), themeId), cb.equal(root.get("dimId"), levelVo.getDimId()));
            }
        }).get();
    }

}


