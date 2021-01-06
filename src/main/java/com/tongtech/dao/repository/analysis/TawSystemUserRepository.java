package com.tongtech.dao.repository.analysis;

import com.tongtech.dao.entity.TawSystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TawSystemUserRepository extends JpaSpecificationExecutor<TawSystemUser>, JpaRepository<TawSystemUser, String> {

    @Query(value="select t.* from taw_system_user t join taw_system_userrefrole m on t.id = m.userid  where m.roleid =:roleId",nativeQuery = true)
    List<TawSystemUser> findAllUserByRole(@Param("roleId") Integer roleId);
}
