package com.tongtech.dao.pk;

import lombok.Data;

import java.io.Serializable;
@Data
public class TemplateDataItemDataPK implements Serializable {
    private String dataitemId;   // 表单字段ID
    private String id;        // 数据Id

    public TemplateDataItemDataPK() {}
    public TemplateDataItemDataPK(String dataitemId,String id) {
        this.dataitemId = dataitemId;
        this.id = id;
    }
}
