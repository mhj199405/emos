package com.tongtech.dao.repository;

import com.tongtech.dao.entity.CommonProvince;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CommonProvinceRepository extends JpaRepository<CommonProvince, BigDecimal>, JpaSpecificationExecutor<CommonProvince> {

    @Query(value="select t from CommonProvince t where t.id <> 100000")
    List findAllNoCountry();
}
