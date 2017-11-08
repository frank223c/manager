package com.suny.association.mapper;

import com.suny.association.entity.po.Account;
import com.suny.association.mapper.interfaces.IMapper;
import com.suny.association.entity.po.ApplicationMessage;

/**
 * Comments:   申请修改考勤类型mapper接口映射
 * @author :   孙建荣
 * Create Date: 2017/03/05 23:05
 */

public interface ApplicationMessageMapper extends IMapper<ApplicationMessage> {
    /**
     * 根据查询条件查询总记录
     * @param applicationMessage   考勤记录实体
     * @return   条件查询出来的记录
     */
    int selectCountByParam(ApplicationMessage applicationMessage);
}