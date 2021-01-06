package com.tongtech.service.analysis;


import com.tongtech.common.vo.analysis.DimensionVo;
import com.tongtech.common.vo.analysis.LevelVo;
import com.tongtech.dao.entity.analysis.UdaThemeDim;
import com.tongtech.dao.entity.analysis.UdaThemeDimLevel;

import java.util.List;

/**
 * <p>
 * 自定义分析专题维度值信息 服务类
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
public interface IUdaThemeDimLevelService {
     List<UdaThemeDimLevel> getLevelListByThemeIdAndDimId(Integer themeId, String dimId);
    void buildLevel(UdaThemeDim obj, DimensionVo dimVo);
    UdaThemeDimLevel findUdaThemeDimByLevelVo(LevelVo levelVo, String themeId);
}
