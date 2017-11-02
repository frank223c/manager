package com.suny.association.entity.po;

/**
 * @author sunjianrong
 */
public class PunchType {
    private Integer punchTypeId;

    private String punchTypeName;

    public PunchType(Integer punchTypeId, String punchTypeName) {
        this.punchTypeId = punchTypeId;
        this.punchTypeName = punchTypeName;
    }

    public PunchType() {
        super();
    }

    public Integer getPunchTypeId() {
        return punchTypeId;
    }

    public void setPunchTypeId(Integer punchTypeId) {
        this.punchTypeId = punchTypeId;
    }

    public String getPunchTypeName() {
        return punchTypeName;
    }

    public void setPunchTypeName(String punchTypeName) {
        this.punchTypeName = punchTypeName == null ? null : punchTypeName.trim();
    }
}