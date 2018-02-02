package com.suny.association.async.handler;

import com.suny.association.async.EventHandler;
import com.suny.association.async.EventModel;
import com.suny.association.async.EventType;

import java.util.Arrays;
import java.util.List;

/**************************************
 *  Description  延迟处理系统日志事件逻辑
 *  @author 孙建荣
 *  @date 18-2-2.下午2:48
 *  @version 1.0
 **************************************/
public class SystemLogHandler implements EventHandler {
    @Override
    public void doHandle(EventModel eventModel) {
        
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOG);
    }
}
