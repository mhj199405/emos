package com.tongtech.service.analysis;


import com.tongtech.common.vo.analysis.TemplateVo;
import com.tongtech.common.vo.analysis.ThemeVo;
import com.tongtech.dao.entity.analysis.UdaTheme;

import java.util.List;

/**
 * <p>
 * 自定义分析专题表 服务类
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
public interface IUdaThemeService {
    List<UdaTheme> findAllUdaThemes();
    List<ThemeVo> buildTheme();
    UdaTheme findUdaThemeByTemplateVo(TemplateVo templateVo);
}
