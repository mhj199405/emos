package com.tongtech.dao.repository;


import com.tongtech.dao.entity.BpmDefProc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface DefProcRepository extends JpaRepository<BpmDefProc, Integer>, JpaSpecificationExecutor<BpmDefProc> {
    @Query(value = "select * from bpm_def_proc_t where proc_id=?1", nativeQuery = true)
    public BpmDefProc getBpmDefProcById(Integer procId);
}
