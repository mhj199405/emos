package com.tongtech.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class VoDefProc implements Serializable {
    //流程id
    private int     procId;

    //节点列表
    List<DefNode> nodes;
    //连接列表
    List<DefLink>   links;
}
