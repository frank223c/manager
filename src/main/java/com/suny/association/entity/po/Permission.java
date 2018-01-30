package com.suny.association.entity.po;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Comments:  权限实体类
 * @author :   孙建荣
 * Create Date: 2017/05/02 12:46
 */
public class Permission implements Serializable {


    private static final long serialVersionUID = -8178626404327345498L;
    /**
     *主键id
     */
    private Integer permissionId;
    /**
     *权限名字，后端使用
     */
    private String permissionName;
    /**
     *权限解释，前端使用
     */
    private String description;
    /**
     *权限创建的时间
     */
    private transient LocalDateTime createTime;
    /**
     * 权限的状态，1为可用，0为不可用
     */
    private boolean permissionStatus;


    public Permission() {
    }

    public Permission(Integer permissionId, String permissionName, String description, LocalDateTime createTime, boolean permissionStatus) {
        this.permissionId = permissionId;
        this.permissionName = permissionName;
        this.description = description;
        this.createTime = createTime;
        this.permissionStatus = permissionStatus;
    }

    public Integer getpermissionId() {
        return permissionId;
    }

    public void setpermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public String getpermissionName() {
        return permissionName;
    }

    public void setpermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public boolean getpermissionStatus() {
        return permissionStatus;
    }

    public void setpermissionStatus(boolean permissionStatus) {
        this.permissionStatus = permissionStatus;
    }
}
