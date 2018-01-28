package com.suny.association.web.validate.code;

import java.io.IOException;

/**************************************
 *  Description  产生验证码接口
 *  @author 孙建荣
 *  @date 18-1-28.下午3:22
 *  @version 1.0
 **************************************/
public interface ValidateCodeGenerator {
    /**
     * 产生验证码
     *
     * @return 验证码
     * @throws IOException IO异常
     */
    ValidateCode generatorCode() throws IOException;
}
