package com.tongtech.auth.data.db_sys_log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SysLogRepository extends JpaRepository<SysLog,Integer>, JpaSpecificationExecutor<SysLog> {
    /**
     * 获取错误的尝试次数
     * @param userId
     * @return
     */
    @Query(value="select t.* from sys_log t where user_id=:userId ORDER BY t.create_time DESC limit 30",nativeQuery=true)
    List<SysLog> findWrongTryNumOfLogin(@Param("userId") Integer userId);
}
