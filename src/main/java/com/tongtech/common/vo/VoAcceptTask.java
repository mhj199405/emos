package com.tongtech.common.vo;

import lombok.Data;

@Data
public class VoAcceptTask {
    private int     insNodeId;          //节点实例id

    private int     optOrganizationId;  //操作机构id
    private int     optPersonId;        //操作人员id
}
