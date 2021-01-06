package com.tongtech.dao.repository.analysis;

import com.tongtech.dao.entity.analysis.UdaTemplateLevelSingle;
import com.tongtech.dao.pk.analysis.UdaTemplateLevelSinglePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UdaTemplateLevelSingleRepository extends JpaRepository<UdaTemplateLevelSingle, UdaTemplateLevelSinglePK>, JpaSpecificationExecutor<UdaTemplateLevelSingle> {
    List<UdaTemplateLevelSingle> findByDimIdAndLevelIdAndTemplateId(String dimId, String levelId, Integer templateId);
}
