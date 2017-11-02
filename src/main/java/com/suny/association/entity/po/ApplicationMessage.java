package com.suny.association.entity.po;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 异议考勤申请实体类
 * @author 孙建荣
 *
 */
public class ApplicationMessage implements Serializable {

    private static final long serialVersionUID = 8887542705964935531L;

    /**
     *主键id
     */
    private Integer applicationId;

    /**
     *成员有异议的考勤记录id
     */
    private PunchRecord punchRecordId;

    /**
     *有异议的考勤类型
     */
    private PunchType punchTypeId;

    /**
     *有异议的理由
     */
    private String applicationReason;

    /**
     *审批的结果
     */
    private CallbackResult applicationResult;

    /**
     *希望更改为什么类型的考勤
     */
    private PunchType changePunchType;

    /**
     *提出申请的时间
     */
    private LocalDateTime applyForTime;

    public ApplicationMessage() {
    }

    public ApplicationMessage(Integer applicationId, PunchRecord punchRecordId, PunchType punchTypeId, String applicationReason, CallbackResult applicationResult, PunchType changePunchType, LocalDateTime applyForTime) {
        this.applicationId = applicationId;
        this.punchRecordId = punchRecordId;
        this.punchTypeId = punchTypeId;
        this.applicationReason = applicationReason;
        this.applicationResult = applicationResult;
        this.changePunchType = changePunchType;
        this.applyForTime = applyForTime;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public PunchRecord getPunchRecordId() {
        return punchRecordId;
    }

    public void setPunchRecordId(PunchRecord punchRecordId) {
        this.punchRecordId = punchRecordId;
    }

    public PunchType getPunchTypeId() {
        return punchTypeId;
    }

    public void setPunchTypeId(PunchType punchTypeId) {
        this.punchTypeId = punchTypeId;
    }

    public String getApplicationReason() {
        return applicationReason;
    }

    public void setApplicationReason(String applicationReason) {
        this.applicationReason = applicationReason;
    }

    public CallbackResult getApplicationResult() {
        return applicationResult;
    }

    public void setApplicationResult(CallbackResult applicationResult) {
        this.applicationResult = applicationResult;
    }

    public PunchType getChangePunchType() {
        return changePunchType;
    }

    public void setChangePunchType(PunchType changePunchType) {
        this.changePunchType = changePunchType;
    }

    public LocalDateTime getApplyForTime() {
        return applyForTime;
    }

    public void setApplyForTime(LocalDateTime applyForTime) {
        this.applyForTime = applyForTime;
    }

    @Override
    public String toString() {
        return "ApplicationMessage{" +
                "applicationId=" + applicationId +
                ", punchRecordId=" + punchRecordId +
                ", punchTypeId=" + punchTypeId +
                ", applicationReason='" + applicationReason + '\'' +
                ", applicationResult=" + applicationResult +
                ", changePunchType=" + changePunchType +
                ", applyForTime=" + applyForTime +
                '}';
    }
}