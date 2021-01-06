package com.tongtech.dao.repository;


import com.tongtech.dao.entity.BpmDefNode;
import com.tongtech.dao.pk.DefNodePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DefNodeRepository extends JpaRepository<BpmDefNode, DefNodePK> {
    @Query(value = "select * from bpm_def_node_t n where n.proc_id=?1 and node_id=?2 ", nativeQuery = true)
    public BpmDefNode getBpmDefNodeByNodeIdAndProcId(int procId, int nodeId);


}
