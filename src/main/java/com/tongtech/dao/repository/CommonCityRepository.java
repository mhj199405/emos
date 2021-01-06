package com.tongtech.dao.repository;

import com.tongtech.dao.entity.CommonBusiDict;
import com.tongtech.dao.entity.CommonCity;
import com.tongtech.dao.pk.CommonBusiDictPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CommonCityRepository extends JpaRepository<CommonCity, BigDecimal>, JpaSpecificationExecutor<CommonCity> {

    @Query(value="select t from CommonCity t where t.regionId =:idStr")
    List<CommonCity> findAllByRegionId(@Param("idStr") BigDecimal idStr);
}
