package com.tongtech.service.analysis;


import com.tongtech.common.vo.analysis.MeasureVo;
import com.tongtech.common.vo.analysis.ThemeVo;
import com.tongtech.dao.entity.analysis.UdaThemeMeasure;

import java.util.List;

/**
 * <p>
 * 自定义分析专题维度指标表 服务类
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
public interface IUdaThemeMeasureService {
    List<UdaThemeMeasure> findAllUdaThemeMeasure();

    void buildMeasure(List<ThemeVo> result);

    UdaThemeMeasure findUdaThemeMeasureByMeasureVo(MeasureVo measureVo, String themeId);

    UdaThemeMeasure findUdaThemeMeasureByFiledName(String fieldName, String themeId);

}
