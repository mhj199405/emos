package com.tongtech.dao.repository.analysis;


import com.tongtech.dao.entity.analysis.UdaTemplateMeasure;
import com.tongtech.dao.pk.analysis.UdaTemplateMeasurePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UdaTemplateMeasureRepository extends JpaRepository<UdaTemplateMeasure, UdaTemplateMeasurePK>, JpaSpecificationExecutor<UdaTemplateMeasure> {
    List<UdaTemplateMeasure> findByTemplateId(Integer templateId);
}
