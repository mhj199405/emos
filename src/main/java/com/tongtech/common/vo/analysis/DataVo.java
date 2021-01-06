package com.tongtech.common.vo.analysis;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DataVo {
    private List<String> columns = new ArrayList<>();
    private List<Row> rows = new ArrayList<>();
}
