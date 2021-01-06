package com.tongtech.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TableAndColumn implements Serializable {

    private String tableName;

    private List<String> columnName;
}
