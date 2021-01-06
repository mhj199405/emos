package com.tongtech.dao.repository.analysis;


import com.tongtech.dao.entity.analysis.UdaCfgDimMap;
import com.tongtech.dao.pk.analysis.UdaCfgDimMapPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UdaCfgDimMapRepository extends JpaRepository<UdaCfgDimMap, UdaCfgDimMapPK>, JpaSpecificationExecutor<UdaCfgDimMap> {
}
