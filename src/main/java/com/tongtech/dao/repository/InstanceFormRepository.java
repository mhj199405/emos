package com.tongtech.dao.repository;

import com.tongtech.dao.entity.FormItemFieldData;
import com.tongtech.dao.entity.InstanceForm;
import com.tongtech.dao.pk.FormItemFieldDataPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InstanceFormRepository extends JpaSpecificationExecutor<InstanceForm>, JpaRepository<InstanceForm,Long > {

    InstanceForm findByInsProcIdAndInsNodeId(Integer insProcId, Integer InsNodeId);

    InstanceForm findFirstByInsProcIdAndAndNodeId(Integer insProcId, Integer nodeId);

}
