package com.tongtech.dao.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="taw_system_role")
public class TawSystemRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;
    private Integer deleted;
    private String deptId;
    private Integer levelId;
    private Integer limitCount;
    private String notes;
    private Integer parentId;
    private String roleName;
    private Integer roletypeId;
    private String singleId;
    private String structureFlag;
    private Integer titleId;
    private Integer workflowFlag;
    private Integer leaf;
    private Integer postid;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public Integer getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(Integer limitCount) {
        this.limitCount = limitCount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRoletypeId() {
        return roletypeId;
    }

    public void setRoletypeId(Integer roletypeId) {
        this.roletypeId = roletypeId;
    }

    public String getSingleId() {
        return singleId;
    }

    public void setSingleId(String singleId) {
        this.singleId = singleId;
    }

    public String getStructureFlag() {
        return structureFlag;
    }

    public void setStructureFlag(String structureFlag) {
        this.structureFlag = structureFlag;
    }

    public Integer getTitleId() {
        return titleId;
    }

    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }

    public Integer getWorkflowFlag() {
        return workflowFlag;
    }

    public void setWorkflowFlag(Integer workflowFlag) {
        this.workflowFlag = workflowFlag;
    }

    public Integer getLeaf() {
        return leaf;
    }

    public void setLeaf(Integer leaf) {
        this.leaf = leaf;
    }

    public Integer getPostid() {
        return postid;
    }

    public void setPostid(Integer postid) {
        this.postid = postid;
    }
}
