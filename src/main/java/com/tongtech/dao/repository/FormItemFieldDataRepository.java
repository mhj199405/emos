package com.tongtech.dao.repository;


import com.tongtech.dao.entity.FormItemFieldData;
import com.tongtech.dao.pk.FormItemFieldDataPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FormItemFieldDataRepository extends JpaSpecificationExecutor<FormItemFieldData>, JpaRepository<FormItemFieldData,FormItemFieldDataPK> {
    @Query("select t from FormItemFieldData t where t.fieldId=?1 order by t.orders")
    public List<FormItemFieldData> findByFieldId(String fieldId);

    @Query("select t from FormItemFieldData t where t.fieldId=?1 and t.id=?2 order by t.id")
    public FormItemFieldData findByFieldIdAndId(String fieldId, String id);

    @Modifying
    @Query("delete from FormItemFieldData t where t.fieldId=?1 ")
    public void deleteByFieldId(String fieldId);

}

