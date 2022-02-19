package com.zjy.service.stratory.close;

import com.zjy.common.stratory.BaseActionParam;
import com.zjy.common.stratory.EventHandlerType;

public class CloseParam implements BaseActionParam {
    @Override
    public EventHandlerType getEventEnum() {
        return EventHandlerType.CLOSE;
    }
}
