package com.suny.association.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by 孙建荣 on 17-9-23.下午9:40
 */
@Component
public class ApplicationContextHolder {
    private static ApplicationContext context;

    public static ApplicationContext get() {
        if (null == context) {
            throw new UnsupportedOperationException("Application is not ready");
        }
        return context;
    }

    @Autowired
    public void setContext(ApplicationContext context) {
        if (null == ApplicationContextHolder.context) {
            ApplicationContextHolder.context = context;
        }
    }
}
