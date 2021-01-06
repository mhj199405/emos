package com.tongtech.service.analysis;


import com.tongtech.dao.entity.analysis.UdaCfgDimMap;

/**
 * <p>
 * 自定义分析专题事实表匹配规则表 服务类
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
public interface IUdaCfgDimMapService  {

    UdaCfgDimMap findUdaCfgDimMap(String themeId, String timeDimLevelId, String geoDimLevelId);
}
