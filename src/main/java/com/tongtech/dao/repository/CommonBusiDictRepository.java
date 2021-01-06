package com.tongtech.dao.repository;

import com.tongtech.dao.entity.BpmDefProc;
import com.tongtech.dao.entity.CommonBusiDict;
import com.tongtech.dao.pk.CommonBusiDictPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonBusiDictRepository extends JpaRepository<CommonBusiDict, CommonBusiDictPk>, JpaSpecificationExecutor<CommonBusiDict> {

}
