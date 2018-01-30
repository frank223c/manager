package com.suny.association.entity.po;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 账号角色实体
 *
 * @author sunjianrong
 */
public class AccountRoles {
    /**
     * 账号主键id
     */
    private Integer roleId;

    /**
     * 账号的中文解释
     */
    private String description;

    /**
     * 账号的角色名字
     */
    private String roleName;

    /**
     * 账号创建的时间
     */
    private LocalDateTime createTime;

    private List<Permission> permissionList = new ArrayList<>();

    public AccountRoles() {
    }

    public AccountRoles(Integer roleId, String description, String roleName, LocalDateTime createTime, List<Permission> permissionList) {
        this.roleId = roleId;
        this.description = description;
        this.roleName = roleName;
        this.createTime = createTime;
        this.permissionList = permissionList;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public List<Permission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }
}