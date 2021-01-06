package com.tongtech.dao.repository.analysis;


import com.tongtech.dao.entity.analysis.UdaTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UdaTemplateRepository extends JpaRepository<UdaTemplate, Integer>, JpaSpecificationExecutor<UdaTemplate> {
    List<UdaTemplate> findByThemeId(int themId);
}
