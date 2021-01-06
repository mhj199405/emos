package com.tongtech.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
@Data
public class BpmDefProc {
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
    private int             busiType;        // 业务类型ID；0：投诉流程；1：业务开通流程；2：业务变更；
    private String          busiName;

    public int getProcId() {
        return procId;
    }

    public void setProcId(int procId) {
        this.procId = procId;
    }

    public String getStdProcId() {
        return stdProcId;
    }

    public void setStdProcId(String stdProcId) {
        this.stdProcId = stdProcId;
    }

    public String getStdProcVersion() {
        return stdProcVersion;
    }

    public void setStdProcVersion(String stdProcVersion) {
        this.stdProcVersion = stdProcVersion;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCreateDeptId() {
        return createDeptId;
    }

    public void setCreateDeptId(String createDeptId) {
        this.createDeptId = createDeptId;
    }

    public String getCreateLoginId() {
        return createLoginId;
    }

    public void setCreateLoginId(String createLoginId) {
        this.createLoginId = createLoginId;
    }

    public LocalDateTime getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(LocalDateTime releaseTime) {
        this.releaseTime = releaseTime;
    }

    public LocalDateTime getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(LocalDateTime effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public LocalDateTime getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(LocalDateTime expiresTime) {
        this.expiresTime = expiresTime;
    }

    public int getReleaseFlag() {
        return releaseFlag;
    }

    public void setReleaseFlag(int releaseFlag) {
        this.releaseFlag = releaseFlag;
    }

    public String getProcDesc() {
        return procDesc;
    }

    public void setProcDesc(String procDesc) {
        this.procDesc = procDesc;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getVerNumber() {
        return verNumber;
    }

    public void setVerNumber(int verNumber) {
        this.verNumber = verNumber;
    }

    public int getBusiType() {
        return busiType;
    }

    public void setBusiType(int busiType) {
        this.busiType = busiType;
    }

    public String getBusiName() {
        return busiName;
    }

    public void setBusiName(String busiName) {
        this.busiName = busiName;
    }
}
