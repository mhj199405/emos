package com.tongtech.common.vo.analysis;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class Row {
    List<Object> values = new LinkedList<>();
}
