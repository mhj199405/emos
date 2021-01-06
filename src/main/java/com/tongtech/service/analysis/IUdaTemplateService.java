package com.tongtech.service.analysis;

//import com.baomidou.mybatisplus.extension.service.IService;


import com.tongtech.common.vo.analysis.TemplateVo;
import com.tongtech.dao.entity.analysis.UdaTemplate;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义分析模板表 服务类
 * </p>
 *
 * @author  
 * @since 2020-06-29
 */
public interface IUdaTemplateService {

    Map<String,Object> queryDataByTemplate(TemplateVo tplVo, Integer pageSize, Integer pageNum);

    ResponseEntity<Resource> exportDataForTemplate(TemplateVo model);
    /**
     * 保存udaTemplate模版
     * @param udaTemplate
     */
    void save(UdaTemplate udaTemplate);

    /**
     * 通过ThemeId来查找UdaTemplate
     * @param themId
     * @return  List<UdaTemplate>
     */
    List<UdaTemplate> findByThemeId(int themId);
}
