package com.suny.association.async;

import java.util.List;

/**
 * 事件处理接口.
 * @author 孙建荣 on 17-9-28.下午4:58
 */
public interface EventHandler {


    void doHandle(EventModel eventModel);


    List<EventType> getSupportEventTypes();


}
