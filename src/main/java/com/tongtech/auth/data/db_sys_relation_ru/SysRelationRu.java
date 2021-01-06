package com.tongtech.auth.data.db_sys_relation_ru;

import lombok.Data;

import javax.persistence.*;

/**
 * CREATE TABLE sys_relation_ru (
 *   id                int AUTO_INCREMENT,
 *   user_id           varchar(20)   DEFAULT NULL,
 *   role_id           varchar(20)   DEFAULT NULL,
 *
 *   primary key (id),
 *   KEY user_id (user_id),
 *   KEY role_id (role_id),
 *   CONSTRAINT sys_relation_ru_ibfk_1 FOREIGN KEY (user_id) REFERENCES sys_user (id) ON DELETE CASCADE ON UPDATE CASCADE,
 *   CONSTRAINT sys_relation_ru_ibfk_2 FOREIGN KEY (role_id) REFERENCES sys_role (id) ON DELETE CASCADE ON UPDATE CASCADE
 * );
 */
@Entity
@Data
@Table(name = "sys_relation_ru")
public class SysRelationRu {
    @Id
//    @SequenceGenerator(name = "relation_ru_seq",sequenceName = "relation_ru_seq",allocationSize = 1,initialValue = 2 )
//    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "relation_ru_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer roleId;
}
