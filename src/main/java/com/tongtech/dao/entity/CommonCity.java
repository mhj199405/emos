package com.tongtech.dao.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * CREATE TABLE `common_city` (
 *   `CITY_ID` decimal(8,0) NOT NULL,
 *   `CITY_NAME` varchar(64) NOT NULL,
 *   `PROVINCE_ID` decimal(8,0) NOT NULL,
 *   `PROVINCE_NAME` varchar(32) NOT NULL,
 *   `REGION_ID` decimal(8,0) NOT NULL,
 *   `REGION_NAME` varchar(64) NOT NULL,
 *   `ORDER_NUM` decimal(4,0) NOT NULL,
 *   `MAP_ID` decimal(4,0) DEFAULT NULL,
 *   `LONGITUDE` decimal(12,6) DEFAULT NULL,
 *   `LATITUDE` decimal(12,6) DEFAULT NULL,
 *   `ZOOM_LEVEL` decimal(2,0) DEFAULT NULL,
 *   PRIMARY KEY (`CITY_ID`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=gbk;
 */
@Entity
@Data
@Table(name = "common_city")
public class CommonCity {

    @Id
    @Column(name="city_id")
    private BigDecimal id;

    @Column(name="city_name")
    private String name;

    private BigDecimal provinceId;

    private String provinceName;

    private BigDecimal regionId;

    private String regionName;

    private BigDecimal orderNum;

    private BigDecimal mapId;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private BigDecimal zoomLevel;
}
