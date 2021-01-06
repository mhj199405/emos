package com.tongtech.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "bpm_def_proc_t")
@Data
public class BpmDefProc {
    @Id
    private  Integer procId;
    private  String stdProcId;
    private String stdProcVersion;
    private  String procName;
    private LocalDateTime createTime;
    private String createDeptId;
    private String createLoginId;
    private LocalDateTime releaseTime;
    private LocalDateTime effectiveTime;
    private LocalDateTime expiresTime;
    private Integer releaseFlag;               //发布标记 0:原始, 1:发布
    private String procDesc;     //流程描述
    private Integer timeout;               //超时时间(秒), 0:不超时
    private Integer verNumber;               //ver_number


}
