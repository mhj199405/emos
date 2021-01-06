package com.tongtech.auth.data.db_sys_relation_dd;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * CREATE TABLE sys_relation_dd (
 *   dept_id           int NOT NULL,
 *   data_id           int NOT NULL,
 *   data_name         varchar(255)   DEFAULT NULL,
 *   data_param        varchar(100)   NOT NULL,
 *   data_operation    int NOT NULL COMMENT '0；等于，1：大于，2：小于，3：大于等于，4：小于等于，5：包含，6：介于之间，。。。',
 *   data_value1       bigint DEFAULT NULL,
 *   data_value2       bigint DEFAULT NULL,
 *   control_order     int NOT NULL,
 *   data_desc         varchar(255)   DEFAULT NULL,
 *
 *   PRIMARY KEY (dept_id,data_id),
 *   KEY data_id (data_id),
 *   CONSTRAINT sys_relation_dd_ibfk_1 FOREIGN KEY (dept_id) REFERENCES sys_department (id) ON DELETE CASCADE ON UPDATE CASCADE,
 *   CONSTRAINT sys_relation_dd_ibfk_2 FOREIGN KEY (data_id) REFERENCES sys_data_control (id) ON DELETE CASCADE ON UPDATE CASCADE
 * );
 */
@Entity
@Data
@Table(name = "sys_relation_dd")
@IdClass(SysRelationDdPk.class)
public class SysRelationDd {
    @Id
    private Integer deptId;
    @Id
    private Integer dataId;

    private String dataName;

    private String dataParam;

    private Integer dataOperation;

    private Long dataValue1;

    private Long dataValue2;

    private Integer controlOrder;

    private String dataDesc;

}
