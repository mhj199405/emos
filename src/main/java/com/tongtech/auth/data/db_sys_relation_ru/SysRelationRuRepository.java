package com.tongtech.auth.data.db_sys_relation_ru;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SysRelationRuRepository extends JpaRepository<SysRelationRu, Integer>, JpaSpecificationExecutor<SysRelationRu> {

    /**
     * 根据角色id查询所有的条目
     * @param roleId
     * @return
     */
    List<SysRelationRu> findAllByRoleId(Integer roleId);

    /**
     * 根据用户id查询所有的条目
     * @param userId
     * @return
     */
    List<SysRelationRu> findAllByUserId(Integer userId);
}
