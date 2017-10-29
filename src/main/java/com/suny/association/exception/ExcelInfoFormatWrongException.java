package com.suny.association.exception;

/**************************************
 *  Description  上传Excel文件时文件格式出错异常
 *  @author 孙建荣
 *  @date 17-10-29.下午6:47
 *  @version 1.0
 **************************************/
public class ExcelInfoFormatWrongException extends RuntimeException {
    public ExcelInfoFormatWrongException(String message) {
        super(message);
    }
}
