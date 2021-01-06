package com.tongtech.auth.data.db_sys_menu;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * CREATE TABLE sys_menu (
 *   id                int NOT NULL AUTO_INCREMENT,
 *   menu_name         varchar(255)   NOT NULL COMMENT '名称',
 *   menu_url          varchar(255)   DEFAULT NULL COMMENT '地址',
 *   open_type         varchar(15)   DEFAULT NULL COMMENT '打开方式',
 *   parent_id         int DEFAULT NULL COMMENT '父节点',
 *   menu_component    varchar(100)   DEFAULT NULL COMMENT '组件',
 *   menu_icon         varchar(100)   DEFAULT NULL COMMENT '图标',
 *   menu_order        int DEFAULT '0' COMMENT '排序字段',
 *   menu_type         int NOT NULL DEFAULT '0' COMMENT '菜单类型',
 *   is_show           int NOT NULL DEFAULT '1' COMMENT '是否显示',
 *   is_route          int DEFAULT '0' COMMENT '是否路由',
 *   is_menu           int DEFAULT '1' COMMENT '是否菜单1:是；0：否',
 *   issued            int DEFAULT '0' COMMENT ' 0:priveate,1:protected,2:public',
 *   menu_status       int DEFAULT '1' COMMENT '菜单状态',
 *   create_time       datetime NOT NULL,
 *
 *   PRIMARY KEY (id)
 * );
 */
@Entity
@Table(name = "sys_menu")
@Data
public class DbSysMenu {
    @Id//一个没有业务意义的主键
//    @SequenceGenerator(name = "sys_menu_seq",sequenceName = "sys_menu_seq",allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator ="sys_menu_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //资源id

    private String menuName;//名称

    private String menuUrl;//地址

    private String openType;//打开方式

    private Integer parentId;//父节点

    private String menuComponent;//组件

    private String menuIcon;//图标

    private Integer menuOrder;//排序字段

    private Integer menuType;//菜单类型

    private Integer isShow;//是否显示

    private Integer isRoute;//是否路由

    private Integer isMenu;//是否菜单

    private Integer issued;

    private Integer menuStatus; //菜单状态

    private String methodType;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getMenuComponent() {
        return menuComponent;
    }

    public void setMenuComponent(String menuComponent) {
        this.menuComponent = menuComponent;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getIsRoute() {
        return isRoute;
    }

    public void setIsRoute(Integer isRoute) {
        this.isRoute = isRoute;
    }

    public Integer getIsMenu() {
        return isMenu;
    }

    public void setIsMenu(Integer isMenu) {
        this.isMenu = isMenu;
    }

    public Integer getIssued() {
        return issued;
    }

    public void setIssued(Integer issued) {
        this.issued = issued;
    }

    public Integer getMenuStatus() {
        return menuStatus;
    }

    public void setMenuStatus(Integer menuStatus) {
        this.menuStatus = menuStatus;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
