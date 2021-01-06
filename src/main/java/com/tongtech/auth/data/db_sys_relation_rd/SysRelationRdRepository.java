package com.tongtech.auth.data.db_sys_relation_rd;

import com.tongtech.auth.data.db_sys_data_control.SysDataControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SysRelationRdRepository extends JpaRepository<SysRelationRd,Integer>, JpaSpecificationExecutor<SysRelationRd> {

    /**
     * 根据角色id和dataId进行删除
     * @param roleId
     * @param dataId
     */
    @Transactional
    @Modifying
    @Query(value="delete from sys_relation_rd where sys_relation_rd.role_id=:roleId and sys_relation_rd.data_id=:dataId",nativeQuery=true)
    void deleteByRoleIdAndDataIds(@Param("roleId") Integer roleId, @Param("dataId") Integer dataId);

    @Query(value=" select data_id as id,data_name,data_param,data_operation,data_value1,data_value2,data_desc " +
            "       from sys_relation_rd where role_id=:roleId ORDER BY control_order desc",nativeQuery=true)
    List<SysDataControl> getDataControlListByRoleId(@Param("roleId") Integer id);

    /**
     * 根据角色id进行删除
     * @param roleId
     */
    @Transactional
    void deleteAllByRoleId(Integer roleId);
}
