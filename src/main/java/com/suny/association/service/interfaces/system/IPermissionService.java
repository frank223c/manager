package com.suny.association.service.interfaces.system;

import com.suny.association.entity.po.Permission;
import com.suny.association.entity.po.PermissionAllot;
import com.suny.association.service.IBaseService;

import java.util.List;

/**
 * Comments:    权限的具体管理接口
 * @author :   孙建荣
 * Create Date: 2017/05/02 13:03
 */
public interface IPermissionService extends IBaseService<Permission>  {
    List<PermissionAllot> queryPermissionQuote(int permissionId);
}
