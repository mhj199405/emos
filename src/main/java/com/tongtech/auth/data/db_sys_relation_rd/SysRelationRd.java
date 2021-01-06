package com.tongtech.auth.data.db_sys_relation_rd;

import lombok.Data;

import javax.persistence.*;

/**
 * CREATE TABLE sys_relation_rd (
 *   id                int AUTO_INCREMENT,
 *   role_id           varchar(20)   NOT NULL,
 *   data_id           int NOT NULL,
 *   data_name         varchar(255)   DEFAULT NULL,
 *   data_param        varchar(100)   NOT NULL,
 *   data_operation    int NOT NULL COMMENT '0；等于，1：大于，2：小于，3：大于等于，4：小于等于，5：包含，6：介于之间，。。。',
 *   data_value1       bigint DEFAULT NULL,
 *   data_value2       bigint DEFAULT NULL,
 *   control_order     int NOT NULL,
 *   data_desc         varchar(255)   DEFAULT NULL,
 *
 *   primary key(id),
 *   KEY role_id (role_id),
 *   KEY data_id (data_id),
 *   CONSTRAINT sys_relation_rd_ibfk_1 FOREIGN KEY (role_id) REFERENCES sys_role (id) ON DELETE CASCADE ON UPDATE CASCADE,
 *   CONSTRAINT sys_relation_rd_ibfk_2 FOREIGN KEY (data_id) REFERENCES sys_data_control (id) ON DELETE CASCADE ON UPDATE CASCADE
 * );
 */
@Entity
@Data
@Table(name = "sys_relation_rd")
public class SysRelationRd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer roleId;

    private Integer dataId;

    private String dataName;

    private String dataParam;

    private Integer dataOperation;

    private Long dataValue1;

    private Long dataValue2;

    private Integer controlOrder;

    private String dataDesc;
}
