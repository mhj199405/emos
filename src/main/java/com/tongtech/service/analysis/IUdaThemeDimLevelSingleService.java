package com.tongtech.service.analysis;


import com.tongtech.common.vo.analysis.LevelVo;
import com.tongtech.dao.entity.analysis.UdaThemeDimLevel;
import com.tongtech.dao.entity.analysis.UdaThemeDimLevelSingle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUdaThemeDimLevelSingleService  {
    List<UdaThemeDimLevelSingle> findAllUdaThemeDimLevelSingle(UdaThemeDimLevel level);
    void buildSingle(LevelVo level1, UdaThemeDimLevel level);

    Page<UdaThemeDimLevelSingle> page(Pageable pageable);
}
