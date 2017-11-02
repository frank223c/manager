package com.suny.association.mapper;

import com.suny.association.mapper.interfaces.IMapper;
import com.suny.association.entity.po.LoginHistory;

import java.util.List;

/**
 * Comments:  登陆历史mapper接口映射
 * @author :   孙建荣
 * Create Date: 2017/03/05 23:05
 */

public interface LoginHistoryMapper extends IMapper<LoginHistory> {
    List<LoginHistory> queryLoginLogByMemberId(int memberId);
}