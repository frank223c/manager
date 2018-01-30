package com.suny.association.entity.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Comments:    角色权限角色中间表，给角色分配权限
 *
 * @author :   孙建荣
 *         Create Date: 2017/05/02 12:44
 */
public class PermissionAllot implements Serializable {

    private static final long serialVersionUID = -2433044880242889682L;
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 角色id
     */
    private AccountRoles roleId;
    /**
     * 权限id
     */
    private List<Permission> permissionArrayList = new ArrayList<>();

    public PermissionAllot() {
    }

    public PermissionAllot(Integer id, AccountRoles roleId, List<Permission> permissionArrayList) {
        this.id = id;
        this.roleId = roleId;
        this.permissionArrayList = permissionArrayList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AccountRoles getRoleId() {
        return roleId;
    }

    public void setRoleId(AccountRoles roleId) {
        this.roleId = roleId;
    }

    public List<Permission> getPermissionArrayList() {
        return permissionArrayList;
    }

    public void setPermissionArrayList(List<Permission> permissionArrayList) {
        this.permissionArrayList = permissionArrayList;
    }
}
