package com.tongtech.auth.data.db_sys_log;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * CREATE TABLE sys_log (
 *   id                varchar(40)   NOT NULL,
 *   user_id           varchar(40)   DEFAULT NULL,
 *   operation         varchar(200)   DEFAULT NULL,
 *   log_type          varchar(20)   DEFAULT NULL,
 *   method            varchar(500)   DEFAULT NULL,
 *   params            text  ,
 *   ip                varchar(60)   DEFAULT NULL,
 *   is_success        int DEFAULT NULL,
 *   create_time       datetime DEFAULT NULL,
 *
 *   PRIMARY KEY (id)
 * );
 */
@Entity
@Data
@Table(name = "sys_log")
public class SysLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private String operation;

    private String logType;

    private String method;

    private String params;

    private String ip;

    private Integer isSuccess;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
