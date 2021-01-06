package com.tongtech.service.analysis.impl;


import com.tongtech.common.utils.SysUtils;
import com.tongtech.common.vo.analysis.DimensionVo;
import com.tongtech.common.vo.analysis.LevelVo;
import com.tongtech.dao.entity.analysis.UdaThemeDim;
import com.tongtech.dao.entity.analysis.UdaThemeDimLevel;
import com.tongtech.dao.repository.analysis.UdaThemeDimLevelRepository;
import com.tongtech.service.analysis.IUdaThemeDimLevelService;
import com.tongtech.service.analysis.IUdaThemeDimLevelSingleService;
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
 * 自定义分析专题维度值信息 服务实现类
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
@Service
public class UdaThemeDimLevelServiceImpl  implements IUdaThemeDimLevelService {

    @Autowired
    UdaThemeDimLevelRepository udaThemeDimLevelRepository;

    @Autowired
    private IUdaThemeDimLevelSingleService iUdaThemeDimLevelSingleService;
    @Override
    public List<UdaThemeDimLevel> getLevelListByThemeIdAndDimId(Integer themeId, String dimId) {
        return  udaThemeDimLevelRepository.getLevelListByThemeIdAndDimId(themeId,dimId );
    }

    @Override
    public void buildLevel(UdaThemeDim obj, DimensionVo dimVo) {
        List<UdaThemeDimLevel> lst = getLevelListByThemeIdAndDimId(
                obj.getThemeId(),
                obj.getDimId()
        );
        if (lst == null || lst.isEmpty()) {
            return;
        }
        LevelVo voObj;
        for(UdaThemeDimLevel level :lst) {
            voObj = new LevelVo();
            BeanUtils.copyProperties(level, voObj, SysUtils.getNullPropertyNames(level));
            voObj.setDimId(obj.getDimId());
            iUdaThemeDimLevelSingleService.buildSingle(voObj,level);
            dimVo.getLevelList().add(voObj);
        }
    }

    @Override
    public UdaThemeDimLevel findUdaThemeDimByLevelVo(LevelVo levelVo, String themeId) {
        return udaThemeDimLevelRepository.findOne(new Specification<UdaThemeDimLevel>() {
            @Override
            public Predicate toPredicate(Root<UdaThemeDimLevel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.and(cb.equal(root.get("themeId"), themeId), cb.equal(root.get("dimId"), levelVo.getDimId()),cb.equal(root.get("levelId"),levelVo.getLevelId()));
            }
        }).get();
    }
}
