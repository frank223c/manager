package com.suny.association.mapper;

import com.suny.association.mapper.interfaces.IMapper;
import com.suny.association.pojo.po.Member;

import java.util.List;


/**
 * Comments:   成员表mapper映射
 * Author:   孙建荣
 * Create Date: 2017/03/05 23:05
 */

public interface MemberMapper extends IMapper<Member> {
    
    List<Member> queryFreezeManager();
   
    List<Member> queryNormalManager();
    
    List<Member> queryFreezeMember();
    
    List<Member> queryNormalMember();
    
    List<Member> queryAllByConditions(int limit, int offset, String departmentname, int status);
    

}