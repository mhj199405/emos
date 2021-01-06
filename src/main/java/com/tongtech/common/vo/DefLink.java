package com.tongtech.common.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DefLink implements Serializable {
    //连接名称
    private String  linkName;
    //连接描述
    private String  linkDesc;
    //连接类型 1:向前, 2:回退
    private int     linkType;
    //开始节点id
    private int     fromNodeId;
    //结束节点id
    private int     toNodeId;
}
