package com.tongtech.dao.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="common_proc_data_t")
@IdClass(CommonProcDataTPK.class)
public class CommonProcDataT implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="ins_proc_id")
    private Integer insProcId;

    @Column(name="proc_id")
    private Integer procId;

    @Id
    @Column(name="busi_type")
    private String busiType;

    @Id
    @Column(name="key_id")
    private Integer keyId;

    @Id
    @Column(name="key_ins")
    private Integer keyIns;

    @Column(name="key_value")
    private String keyValue;

    @Column(name="create_time")
    private LocalDateTime createTime;

    @Column(name="operation_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;

    @Column(name="ver_number")
    private Integer verNumber;
}

