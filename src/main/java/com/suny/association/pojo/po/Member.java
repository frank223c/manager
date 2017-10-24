package com.suny.association.pojo.po;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * 社团成员实体类
 *
 * @author 孙建荣
 */
public class Member implements Serializable {

    private static final long serialVersionUID = -43708088833959784L;
    /**
     * 社团成员的ID,默认是自增长的
     */
    private Integer memberId;
    /**
     * 成员的中文名字
     */
    private String memberName;
    /**
     * 成员的所在班级,如:软件1班
     */
    private String memberClassName;
    /**
     * 成员的的性别,false【0】为女生,true【1】为男生
     */
    private Boolean memberSex;
    /**
     * 成员的届数,如入学当年为2017年,则值为【2017】
     */
    private Integer memberGradeNumber;
    /**
     * 成员在社团里面对应的一个管理人员信息
     */
    @JsonIgnore
    private Member memberManager;
    /**
     * 成员的部门信息
     */
    private Department memberDepartment;
    /**
     * 成员在部门的状态，false【0】为退会状态,true【1】为在会状态
     */
    private Boolean memberStatus;
    /**
     * 成员在部门里面的部门角色
     */
    private MemberRoles memberRoles;

    public Member() {
    }

    public Member(Integer memberId, String memberName, String memberClassName, Boolean memberSex, Integer memberGradeNumber, Member memberManager, Department memberDepartment, Boolean memberStatus, MemberRoles memberRoles) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberClassName = memberClassName;
        this.memberSex = memberSex;
        this.memberGradeNumber = memberGradeNumber;
        this.memberManager = memberManager;
        this.memberDepartment = memberDepartment;
        this.memberStatus = memberStatus;
        this.memberRoles = memberRoles;
    }


    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberClassName() {
        return memberClassName;
    }

    public void setMemberClassName(String memberClassName) {
        this.memberClassName = memberClassName;
    }

    public Boolean getMemberSex() {
        return memberSex;
    }

    public void setMemberSex(Boolean memberSex) {
        this.memberSex = memberSex;
    }

    public Integer getMemberGradeNumber() {
        return memberGradeNumber;
    }

    public void setMemberGradeNumber(Integer memberGradeNumber) {
        this.memberGradeNumber = memberGradeNumber;
    }

    public Member getMemberManager() {
        return memberManager;
    }

    public void setMemberManager(Member memberManager) {
        this.memberManager = memberManager;
    }

    public Department getMemberDepartment() {
        return memberDepartment;
    }

    public void setMemberDepartment(Department memberDepartment) {
        this.memberDepartment = memberDepartment;
    }

    public Boolean getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(Boolean memberStatus) {
        this.memberStatus = memberStatus;
    }

    public MemberRoles getMemberRoles() {
        return memberRoles;
    }

    public void setMemberRoles(MemberRoles memberRoles) {
        this.memberRoles = memberRoles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Member member = (Member) o;

        if (memberId != null ? !memberId.equals(member.memberId) : member.memberId != null) {
            return false;
        }
        if (memberName != null ? !memberName.equals(member.memberName) : member.memberName != null) {
            return false;
        }
        if (memberClassName != null ? !memberClassName.equals(member.memberClassName) : member.memberClassName != null) {
            return false;
        }
        if (memberSex != null ? !memberSex.equals(member.memberSex) : member.memberSex != null) {
            return false;
        }
        if (memberGradeNumber != null ? !memberGradeNumber.equals(member.memberGradeNumber) : member.memberGradeNumber != null) {
            return false;
        }
        if (memberManager != null ? !memberManager.equals(member.memberManager) : member.memberManager != null) {
            return false;
        }
        if (memberDepartment != null ? !memberDepartment.equals(member.memberDepartment) : member.memberDepartment != null) {
            return false;
        }
        if (memberStatus != null ? !memberStatus.equals(member.memberStatus) : member.memberStatus != null) {
            return false;
        }
        return memberRoles != null ? memberRoles.equals(member.memberRoles) : member.memberRoles == null;

    }

    @Override
    public int hashCode() {
        int result = memberId != null ? memberId.hashCode() : 0;
        result = 31 * result + (memberName != null ? memberName.hashCode() : 0);
        result = 31 * result + (memberClassName != null ? memberClassName.hashCode() : 0);
        result = 31 * result + (memberSex != null ? memberSex.hashCode() : 0);
        result = 31 * result + (memberGradeNumber != null ? memberGradeNumber.hashCode() : 0);
        result = 31 * result + (memberManager != null ? memberManager.hashCode() : 0);
        result = 31 * result + (memberDepartment != null ? memberDepartment.hashCode() : 0);
        result = 31 * result + (memberStatus != null ? memberStatus.hashCode() : 0);
        result = 31 * result + (memberRoles != null ? memberRoles.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Member{" +
                "成员Id=" + memberId +
                ", 成员名字='" + memberName + '\'' +
                ", 班级='" + memberClassName + '\'' +
                ", 性别=" + memberSex +
                ", 届级=" + memberGradeNumber +
                ", 部门=" + memberDepartment +
                ", 状态=" + memberStatus +
                ", 角色=" + memberRoles +
                '}';
    }
}