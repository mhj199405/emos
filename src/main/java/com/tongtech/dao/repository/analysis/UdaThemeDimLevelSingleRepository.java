package com.tongtech.dao.repository.analysis;


import com.tongtech.dao.entity.analysis.UdaThemeDimLevelSingle;
import com.tongtech.dao.pk.analysis.UdaThemeDimLevelSinglePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UdaThemeDimLevelSingleRepository extends JpaRepository<UdaThemeDimLevelSingle, UdaThemeDimLevelSinglePK>, JpaSpecificationExecutor<UdaThemeDimLevelSingle> {
}
