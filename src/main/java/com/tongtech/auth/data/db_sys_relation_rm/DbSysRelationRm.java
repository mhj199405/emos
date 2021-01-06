package com.tongtech.auth.data.db_sys_relation_rm;

import lombok.Data;

import javax.persistence.*;

/**
 * CREATE TABLE sys_relation_rm (
 *   id                int AUTO_INCREMENT,
 *   role_id           varchar(20)   NOT NULL,
 *   menu_id           int NOT NULL,
 *
 *   primary key (id),
 *   KEY role_id (role_id),
 *   KEY menu_id (menu_id),
 *   CONSTRAINT  sys_relation_rm_ibfk_1 FOREIGN KEY (role_id) REFERENCES sys_role (id) ON DELETE CASCADE ON UPDATE CASCADE,
 *   CONSTRAINT  sys_relation_rm_ibfk_2 FOREIGN KEY (menu_id) REFERENCES sys_menu (id) ON DELETE CASCADE ON UPDATE CASCADE
 * );
 */
@Entity
@Table(name = "sys_relation_rm")
@Data
public class DbSysRelationRm {

    @Id
//    @SequenceGenerator(name = "relation_rm_seq",sequenceName = "relation_rm_seq",allocationSize = 1,initialValue = 2 )
//    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "relation_rm_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer roleId;//角色id

    private Integer menuId;//菜单id

}
