package com.tongtech.common.vo;


import com.tongtech.dao.entity.TemplateDataItem;
import com.tongtech.dao.entity.TemplateDataItemData;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DataItemVO {
    TemplateDataItem dataItem;
    List<TemplateDataItemData> dataItemDataList;
    public void addDataItemData(TemplateDataItemData data) {
        if (dataItemDataList == null) {
            dataItemDataList = new ArrayList<>();
        }
        dataItemDataList.add(data);
    }
}
