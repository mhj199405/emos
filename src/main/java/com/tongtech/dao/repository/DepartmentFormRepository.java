package com.tongtech.dao.repository;

import com.tongtech.dao.entity.DepartmentForm;
import com.tongtech.dao.entity.FormFormItem;
import com.tongtech.dao.pk.DepartmentFormPk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentFormRepository extends JpaSpecificationExecutor<DepartmentForm>, JpaRepository<DepartmentForm, DepartmentFormPk> {

    void deleteAllByFormId(Integer formId);

    DepartmentForm findFirstByUserIdAndFormId(Integer userId, Integer formId);

    @Query(value="select distinct t.form_id from frm_department_form t where t.form_Id in :idList and t.role_Id in :roleIds " ,nativeQuery= true,
    countQuery="select count(distinct t.form_id) from frm_department_form t where t.form_Id in :idList and t.role_Id in :roleIds ")
    Page<Integer> findFormByPage(@Param("idList") List<Long> idList, @Param("roleIds") List<Integer> roleIds, Pageable pageable);

    List<DepartmentForm> findAllByRoleId(Integer id);
}
