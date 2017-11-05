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
    /**
     * 根据条件查询数据库里面的登录记录条数
     * @param loginHistory  实体类查询条件
     * @return   查询条件查询出来的记录
     */
    int selectCountByParam(LoginHistory loginHistory);

    List<LoginHistory> queryLoginLogByMemberId(int memberId);
}