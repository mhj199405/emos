package com.tongtech.dao.repository;

import com.tongtech.dao.entity.TemplateDataItemData;
import com.tongtech.dao.pk.TemplateDataItemDataPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TemplateDataItemDataRepository
        extends JpaSpecificationExecutor<TemplateDataItemData>,JpaRepository<TemplateDataItemData, TemplateDataItemDataPK> {
    @Modifying
    @Query("delete  from TemplateDataItemData t where t.dataitemId=?1")
    public void deleteByDataIemId(String dataitemId);

    @Query("select t from TemplateDataItemData t where t.dataitemId=?1 order by t.orders")
    public List<TemplateDataItemData> findbyDataitemId(String dataitemId);

    @Query("select t from TemplateDataItemData t where t.dataitemId=?1 and t.id=?2")
    public TemplateDataItemData findbyDataitemIdAndId(String dataitemId, String id);
}
