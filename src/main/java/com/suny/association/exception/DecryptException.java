package com.suny.association.exception;

/**************************************
 *  Description  解密异常
 *  @author 孙建荣
 *  @date 17-11-2.下午3:32
 *  @version 1.0
 **************************************/
public class DecryptException extends RuntimeException {


    public DecryptException(String message) {
        super(message);
    }

    public DecryptException(String message, Throwable cause) {
        super(message, cause);
    }
}
