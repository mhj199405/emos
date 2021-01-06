package com.tongtech.dao.repository.analysis;


import com.tongtech.dao.entity.analysis.UdaThemeDim;
import com.tongtech.dao.pk.analysis.UdaThemeDimPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UdaThemeDimRepository extends JpaRepository<UdaThemeDim, UdaThemeDimPK>, JpaSpecificationExecutor<UdaThemeDim> {


    public UdaThemeDim findByThemeIdAndDimId(Integer themId, String dimId);
}
