package com.tongtech.dao.repository;

import java.util.List;

import com.tongtech.dao.entity.FormItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface FormItemRepository extends JpaSpecificationExecutor<FormItem>, JpaRepository<FormItem, String> {
    @Query("select t from FormItem t where t.layerId=?1 order by t.orders")
    public List<FormItem> findByLayerId(Long layerId);

    @Query("select a from FormItem a, FormFormItem b where a.layerId=b.id and b.formId=?1 order by b.orders, a.orders,a.id")
    public List<FormItem> findByFormId(Long formId);

    @Modifying
    @Query("delete from FormItem t where t.layerId=?1")
    public void deleteByFormFormItem(Long id);

    @Query("select t from FormItem  t where t.layerId in(select item.id from FormFormItem item where item.formId=?1 and item.isList=0 ) and  t.isDisplay=1 order by  t.orders")
    public List<FormItem>  findByLayerIdAndIsDisplay(Long formId);


    @Query("select t from FormItem  t where t.layerId in(select item.id from FormFormItem item where item.formId=?1  ) and  t.isPrimaryKey=1 order by  t.orders")
    public FormItem  findByLayerIdAndIsPrimaryKey(Long formId);

}
