package com.tongtech.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
@Data
public class BpmDefProcVo {
        private int             procId;         //流程id
        private String          stdProcId;      //规范流程id
        private String          stdProcVersion; //规范流程版本
        private String          procName;       //流程名称
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createTime;     //创建时间
        private String          createDeptId;   //创建部门id
        private String          createLoginId;  //创建登录id
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime   releaseTime;    //发布时间
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime   effectiveTime;  //生效时间
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime   expiresTime;    //过期时间
        private int             releaseFlag;    //发布标记 0:原始, 1:发布
        private String          procDesc;       //流程描述
        private int             timeout;        //超时时间(秒), 0:不超时
        private int             verNumber;      //ver_number
        private String          busiType;           // 流程类别
        private String          busiName;       //tmp

        private String          createDeptName;
        private String          createLoginName;
}
