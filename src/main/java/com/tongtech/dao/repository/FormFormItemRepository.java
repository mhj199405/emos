package com.tongtech.dao.repository;

import com.tongtech.dao.entity.FormFormItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormFormItemRepository extends JpaSpecificationExecutor<FormFormItem>, JpaRepository<FormFormItem, Long> {

    /**
     * 查找formnode
     * @param id
     * @return
     */
    @Query("select t from FormFormItem t where t.formId=?1 order by t.orders")
    List<FormFormItem> findByFormId(Long id);

    @Query("select t from FormFormItem t where t.formId in (select a.id from Form a where a.isTemplate=1)  order by t.id")
    List<FormFormItem> findAllTemplateFormFormItem();

    @Query("select t from FormFormItem t where t.formId in (select a.id from Form a where a.isTemplate=1 and a.id=?1)  order by t.id")
    public List<FormFormItem> findAllTemplateFormFormItem(Long id);

    /**
     * 获取所有的节点
     * @return
     */
    @Query("select t from FormFormItem t where t.isTemplate=1 and t.formId=-1 order by t.orders")
    List<FormFormItem> getAllFormNode();

    @Query("select t from FormFormItem t where t.isTemplate=1 order by t.id")
    public List<FormFormItem> findAllTemplateSection();

    FormFormItem findFirstByFormIdAndParentIdAndName(Long id, Long id1, String sectionName);

    @Modifying
    @Query("update FormFormItem t set t.orders=?1 where t.id=?2")
    public void updateOrder(Long order, Long id);
}
