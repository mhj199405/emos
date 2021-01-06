package com.tongtech.dao.repository.analysis;


import com.tongtech.dao.entity.analysis.UdaThemeDimLevel;
import com.tongtech.dao.pk.analysis.UdaThemeDimLevelPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UdaThemeDimLevelRepository extends JpaRepository<UdaThemeDimLevel, UdaThemeDimLevelPK>, JpaSpecificationExecutor<UdaThemeDimLevel> {
    @Query(value="select * from uda_theme_dim_level  where theme_Id=?1 and dim_Id=?2",nativeQuery = true)
    List<UdaThemeDimLevel> getLevelListByThemeIdAndDimId(Integer themeId, String dimId);
}
