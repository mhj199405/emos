package com.tongtech.service.analysis;


import com.tongtech.dao.entity.analysis.UdaTemplateLevelSingle;

import java.util.List;

public interface IUdaTemplateLevelSingleService  {
    /**
     * 查询UdaTemplateLevelSingle
     * @param dimId
     * @param levelId
     * @param templateId
     * @return 返回一个列表
     */
    List<UdaTemplateLevelSingle> findByDimIdAndGetLevelIdAndTemplatedId(String dimId, String levelId, Integer templateId);

    /**
     * 保存udaTemplateLevelSingle
     * @param udaTemplateLevelSingleList
     */
    void saveBatch(List<UdaTemplateLevelSingle> udaTemplateLevelSingleList);
}
