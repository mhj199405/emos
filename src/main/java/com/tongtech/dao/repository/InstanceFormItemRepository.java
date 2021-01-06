package com.tongtech.dao.repository;

import com.tongtech.dao.entity.FormItemFieldData;
import com.tongtech.dao.entity.InstanceFormItem;
import com.tongtech.dao.pk.FormItemFieldDataPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstanceFormItemRepository extends JpaSpecificationExecutor<InstanceFormItem>, JpaRepository<InstanceFormItem, Long> {

    List<InstanceFormItem> findAllByFormInstId(Integer formInstId);
}
