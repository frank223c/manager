package com.suny.association.web.validate.code;

import java.time.LocalDateTime;

/**************************************
 *  Description  验证码抽象类
 *  @author 孙建荣
 *  @date 18-1-28.下午3:13
 *  @version 1.0
 **************************************/
public abstract class ValidateCode {
    private String code;

    private LocalDateTime expireTime;

    public ValidateCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    public ValidateCode(String code, int expireTime) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireTime);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
