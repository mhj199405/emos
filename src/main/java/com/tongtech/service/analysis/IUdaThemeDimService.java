package com.tongtech.service.analysis;


import com.tongtech.common.vo.analysis.LevelVo;
import com.tongtech.common.vo.analysis.ThemeVo;
import com.tongtech.dao.entity.analysis.UdaThemeDim;

import java.util.List;

/**
 * <p>
 * 自定义分析专题维度维度表 服务类
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
public interface IUdaThemeDimService  {

    List<UdaThemeDim> findAllUdaThemeDim();
    void buildDim(List<ThemeVo> result);
    UdaThemeDim findUdaThemeDimByLevelVo(LevelVo levelVo, String themeId);
}
