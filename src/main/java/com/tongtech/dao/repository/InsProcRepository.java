package com.tongtech.dao.repository;


import com.tongtech.dao.entity.BpmInsProc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InsProcRepository extends JpaRepository<BpmInsProc,Integer>, JpaSpecificationExecutor<BpmInsProc> {

    @Query(value = "select proc_id from bpm_ins_proc_t where ins_proc_id=?1", nativeQuery = true)
    public Integer getProcIdByInsProcId(int insProcId);

    @Query(value = "select * from bpm_ins_proc_t where ins_proc_id=?1", nativeQuery = true)
    public BpmInsProc getInsProcByInsProcId(int insProcId);


    @Query(value = "select ins_proc_id,proc_id,opt_persion_id from bpm_ins_proc_t where proc_id=?1 and opt_persion_id=?2", nativeQuery = true)
    public List<BpmInsProc> getInsProcByProcIdAndUserId(int procId, int userId);

    @Query(value = "select ins_proc_id from bpm_ins_proc_t where proc_id=?1", nativeQuery = true)
    public List<Integer> getInsProcSumByProcId(int procId);
}
