package com.tongtech.dao.repository.analysis;


import com.tongtech.dao.entity.TawSystemUserRefRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TawSystemUserRefRoleRepository extends JpaSpecificationExecutor<TawSystemUserRefRole>, JpaRepository<TawSystemUserRefRole, String> {

    void deleteAllByUserid(String userId);
}
