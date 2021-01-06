package com.tongtech.dao.pk;


import lombok.Data;

import java.io.Serializable;

@Data
public class FormItemFieldDataPK implements Serializable {
    private String fieldId;   // 表单字段ID
    private String id;        // 数据Id
}

