package com.tongtech.dao.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * CREATE TABLE `common_province` (
 *   `PROVINCE_ID` decimal(8,0) NOT NULL,
 *   `PROVINCE_NAME` varchar(32) NOT NULL,
 *   `PROVINCE_CODE` varchar(8) NOT NULL,
 *   `PROVINCE_SHORT_ID` decimal(4,0) NOT NULL,
 *   `NAME_ABBR` varchar(8) NOT NULL,
 *   `ORDER_NUM` decimal(4,0) NOT NULL,
 *   `MAP_ID` decimal(4,0) DEFAULT NULL,
 *   PRIMARY KEY (`PROVINCE_ID`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=gbk;
 */
@Entity
@Table(name = "common_province")
@Data
public class CommonProvince {

    @Id
    @Column(name = "province_id" )
    private BigDecimal id;

    @Column(name= "province_name")
    private String name;

    private String provinceCode;

    private BigDecimal provinceShortId;

    private String nameAbbr;

    private BigDecimal orderNum;

    private BigDecimal mapId;
}
