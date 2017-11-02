package com.suny.association.entity.po;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录历史实体类
 * @author sunjianrong
 */
public class LoginHistory implements Serializable {

    private static final long serialVersionUID = -8878807427599819429L;
    private Long loginHistoryId;

    private String loginName;

    private String loginIp;

    private String loginAddress;

    private transient LocalDateTime loginTime;

    private String loginBrowser;

    private String loginOsVersion;

    private String loginUserAgent;

    private boolean loginStatus;

    public LoginHistory() {
    }

    public LoginHistory(String loginName, String loginIp, String loginAddress, LocalDateTime loginTime, String loginBrowser, String loginOsVersion, String loginUserAgent, boolean loginStatus) {
        this.loginName = loginName;
        this.loginIp = loginIp;
        this.loginAddress = loginAddress;
        this.loginTime = loginTime;
        this.loginBrowser = loginBrowser;
        this.loginOsVersion = loginOsVersion;
        this.loginUserAgent = loginUserAgent;
        this.loginStatus = loginStatus;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getLoginHistoryId() {
        return loginHistoryId;
    }

    public void setLoginHistoryId(Long loginHistoryId) {
        this.loginHistoryId = loginHistoryId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginAddress() {
        return loginAddress;
    }

    public void setLoginAddress(String loginAddress) {
        this.loginAddress = loginAddress;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginBrowser() {
        return loginBrowser;
    }

    public void setLoginBrowser(String loginBrowser) {
        this.loginBrowser = loginBrowser;
    }

    public String getLoginOsVersion() {
        return loginOsVersion;
    }

    public void setLoginOsVersion(String loginOsVersion) {
        this.loginOsVersion = loginOsVersion;
    }

    public String getLoginUserAgent() {
        return loginUserAgent;
    }

    public void setLoginUserAgent(String loginUserAgent) {
        this.loginUserAgent = loginUserAgent;
    }

    public boolean isLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    @Override
    public String toString() {
        return "LoginHistory{" +
                "loginHistoryId=" + loginHistoryId +
                ", loginName='" + loginName + '\'' +
                ", loginIp='" + loginIp + '\'' +
                ", loginAddress='" + loginAddress + '\'' +
                ", loginTime=" + loginTime +
                ", loginBrowser='" + loginBrowser + '\'' +
                ", loginOsVersion='" + loginOsVersion + '\'' +
                ", loginUserAgent='" + loginUserAgent + '\'' +
                ", loginStatus=" + loginStatus +
                '}';
    }
}