package com.tongtech.dao.repository.analysis;

import com.tongtech.dao.entity.TawSystemDept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TawSystemDeptRepository  extends JpaSpecificationExecutor<TawSystemDept>, JpaRepository<TawSystemDept, Integer> {

    List<TawSystemDept> findAllByParentdeptid(String parentDeptId);

    @Query(value="select t from TawSystemDept t where  t.deptname like ?1")
    List<TawSystemDept> findAllByDeptNameAndDeptIds(String deptName, String deptId);

    @Query(value="select  m.* from taw_system_user t  JOIN taw_system_dept m on t.deptid = m.deptid where t.userid=:userId",nativeQuery=true)
    TawSystemDept findDeptByUserId(@Param("userId") String userId);

    TawSystemDept findByDeptid(String deptId);

}
