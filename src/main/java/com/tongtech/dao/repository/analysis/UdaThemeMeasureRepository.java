package com.tongtech.dao.repository.analysis;


import com.tongtech.dao.entity.analysis.UdaThemeMeasure;
import com.tongtech.dao.pk.analysis.UdaThemeMeasurePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UdaThemeMeasureRepository extends JpaRepository<UdaThemeMeasure, UdaThemeMeasurePK>, JpaSpecificationExecutor<UdaThemeMeasure> {
}
