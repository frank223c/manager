package com.suny.association.service.interfaces.core;

import com.suny.association.entity.po.PunchRecord;
import com.suny.association.entity.po.PunchType;
import com.suny.association.service.IBaseService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Comments:
 * @author :   孙建荣
 * Create Date: 2017/03/07 22:15
 */
public interface IPunchRecordService extends IBaseService<PunchRecord> {
    void updatePunchType(PunchRecord punchRecord, PunchType punchType);

    int updatePunch(@Param("memberId") Integer memberId, @Param("punchRecordId") Long punchRecordId);

    PunchRecord queryByMemberIdAndDate(Integer memberId);

    int batchInsertsPunchRecord();

    List<PunchRecord> queryByPunchDate();
}
