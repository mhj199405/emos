package com.tongtech.dao.repository;

import com.tongtech.dao.entity.CommonProvince;
import com.tongtech.dao.entity.CommonRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CommonRegionRepository extends JpaRepository<CommonRegion, BigDecimal>, JpaSpecificationExecutor<CommonRegion> {

    @Query(value="select t from CommonRegion t where  t.provinceId =:idStr")
    List<CommonRegion> findAllByProvinceId(@Param("idStr") BigDecimal idStr);
}
