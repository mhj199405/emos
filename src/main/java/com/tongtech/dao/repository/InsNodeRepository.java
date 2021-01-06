package com.tongtech.dao.repository;


import com.tongtech.dao.entity.BpmInsNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InsNodeRepository extends JpaRepository<BpmInsNode,Integer>, JpaSpecificationExecutor<BpmInsNode> {

    @Query(value = "select * from bpm_ins_node_t  where ins_proc_id=?1",nativeQuery = true)
    public List<BpmInsNode> getInsNodeByInsProcId(int insProcId);
}
