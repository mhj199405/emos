package com.tongtech.dao.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * CREATE TABLE `common_region` (
 *   `REGION_ID` decimal(8,0) NOT NULL,
 *   `REGION_NAME` varchar(64) NOT NULL,
 *   `REGION_CODE` decimal(8,0) NOT NULL,
 *   `PCODE` decimal(8,0) NOT NULL,
 *   `PROVINCE_ID` decimal(8,0) NOT NULL,
 *   `PROVINCE_NAME` varchar(32) NOT NULL,
 *   `ORDER_NUM` decimal(4,0) NOT NULL,
 *   `MAP_ID` decimal(4,0) DEFAULT NULL,
 *   `LONGITUDE` decimal(12,6) DEFAULT NULL,
 *   `LATITUDE` decimal(12,6) DEFAULT NULL,
 *   `ZOOM_LEVEL` decimal(2,0) DEFAULT NULL,
 *   `REGION_JC` varchar(64) DEFAULT NULL,
 *   PRIMARY KEY (`REGION_ID`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=gbk;
 */

@Entity
@Table(name = "common_region")
@Data
public class CommonRegion {
    @Id
    @Column(name = "region_id")
    private BigDecimal id;

    @Column(name="region_name")
    private String name;

    private BigDecimal regionCode;

    private BigDecimal pcode;

    private BigDecimal provinceId;

    private String provinceName;

    private BigDecimal orderNum;

    private BigDecimal mapId;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private BigDecimal zoomLevel;

    private String regionJc;

}
