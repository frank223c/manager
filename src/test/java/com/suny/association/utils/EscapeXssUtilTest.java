package com.suny.association.utils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**************************************
 *  Description  
 *  @author 孙建荣
 *  @date 17-11-3.下午8:07
 *  @version 1.0
 **************************************/
public class EscapeXssUtilTest {
    private static Logger logger = LoggerFactory.getLogger(EscapeXssUtilTest.class);
    @Test
    public void escapeXss() throws Exception {
        String script="<script>这是脚本</script>我是";
        String s = EscapeXssUtil.escapeXss(script);
          logger.info("过滤后的字符是======={}",s);
    }

}