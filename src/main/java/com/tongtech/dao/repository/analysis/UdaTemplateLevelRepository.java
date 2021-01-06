package com.tongtech.dao.repository.analysis;


import com.tongtech.dao.entity.analysis.UdaTemplateLevel;
import com.tongtech.dao.pk.analysis.UdaTemplateLevelPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UdaTemplateLevelRepository extends JpaRepository<UdaTemplateLevel, UdaTemplateLevelPK>, JpaSpecificationExecutor<UdaTemplateLevel> {
    List<UdaTemplateLevel> findByTemplateId(Integer templateId);
}
