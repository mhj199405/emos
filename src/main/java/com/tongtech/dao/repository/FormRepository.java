package com.tongtech.dao.repository;

import com.tongtech.dao.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormRepository extends JpaRepository<Form,Long> , JpaSpecificationExecutor<Form> {

    /**
     * 查找所有的模板
     * @return
     */
    @Query("select t from Form t where t.isTemplate = 1 and t.status='1' order by t.createTime desc")
    List<Form> findFormTemplates();

    /**
     * 查询所有的表单模板
     * @return
     */
    @Query("select t from Form t where t.isTemplate = 1 and t.status='1' order by t.createTime desc  ")
    List<Form> getFormTemplate();
    @Query("select max(t.versions) from Form t where t.name=?1 and t.status='1'")
    Integer findMaxVersion(String name);

    @Query("select distinct t from DepartmentForm m left join Form t on m.formId=t.id where m.roleId in:roleList order by t.createTime")
    List<Form> findAllFormByDepartmentId(@Param("roleList") List<Integer> roleList);

    @Query("select t from Form t where t.name=?1 and t.status='1' order by t.versions desc,t.id desc")
    List<Form> findByName(String nameStr);

    /**
     * 查询所有的id
     * @return
     */
    @Query("select distinct t.id from Form  t ")
    List<Long> findAllIds();

    /**
     * 根据表明查找所有的id
     * @param tableName
     * @return
     */
    @Query("select distinct t.id from Form t where t.name like :formName")
    List<Long> findAllIdsByTableName(@Param("formName") String tableName);
}
