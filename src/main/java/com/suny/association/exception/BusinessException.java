package com.suny.association.exception;

/**
 * Comments:  Business异常
 * @author :   孙建荣
 * Create Date: 2017/03/08 18:15
 */
public class BusinessException extends RuntimeException{

    public BusinessException(Object obj) {
        super(obj.toString());
    }
    
    
}
