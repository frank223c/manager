package com.suny.association.exception;

/**************************************
 *  Description   加密异常
 *  @author 孙建荣
 *  @date 17-11-2.下午3:33
 *  @version 1.0
 **************************************/
public class EncryptException extends RuntimeException {


    public EncryptException(String message) {
        super(message);
    }

    public EncryptException(String message, Throwable cause) {
        super(message, cause);
    }
}
