package com.tongtech.auth.data.db_sys_relation_dm;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * CREATE TABLE sys_relation_dm (
 *   dept_id           int NOT NULL,
 *   menu_id           int NOT NULL,
 *
 *   PRIMARY KEY (dept_id,menu_id),
 *   KEY menu_id (menu_id),
 *   CONSTRAINT sys_relation_dm_ibfk_1 FOREIGN KEY (dept_id) REFERENCES sys_data_control (id) ON DELETE CASCADE ON UPDATE CASCADE,
 *   CONSTRAINT sys_relation_dm_ibfk_2 FOREIGN KEY (menu_id) REFERENCES sys_menu (id) ON DELETE CASCADE ON UPDATE CASCADE
 * );
 */
@Entity
@Data
@Table(name = "sys_relation_dm")
@IdClass(SysRelationDmPk.class)
public class SysRelationDm {
    @Id
    private Integer deptId;
    @Id
    private Integer menuId;
}
