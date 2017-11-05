package com.suny.association.service.interfaces.system;

import com.suny.association.entity.po.LoginHistory;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.service.IBaseService;

import java.util.List;

/**
 * Comments:
 * @author :   孙建荣
 * Create Date: 2017/03/07 22:12
 */
public interface ILoginHistoryService extends IBaseService<LoginHistory> {

    /**
     * 根据条件查询数据库里面的登录记录条数
     * @param loginHistory  实体类查询条件
     * @return   查询条件查询出来的记录
     */
     int selectCountByParam(LoginHistory loginHistory);


    void saveLoginLog(String username, boolean authStatus);

    List<LoginHistory> queryLoginLogByMemberId(int memberId);

//    List<LoginHistory> queryByMemberId(int memberId);
}
