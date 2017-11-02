package com.suny.association.service.interfaces.system;

import com.suny.association.entity.po.PermissionAllot;
import com.suny.association.service.IBaseService;

import java.util.List;

/**
 * Comments:    权限分配业务逻辑接口
 * @author :   孙建荣
 * Create Date: 2017/05/02 13:07
 */
public interface IPermissionAllotService extends IBaseService<PermissionAllot> {
    List<PermissionAllot> queryByRoleId(int roleId);
}
