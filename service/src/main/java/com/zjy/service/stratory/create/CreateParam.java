package com.zjy.service.stratory.create;

import com.zjy.common.stratory.BaseActionParam;
import com.zjy.common.stratory.EventHandlerType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateParam implements BaseActionParam {
    @Override
    public EventHandlerType getEventEnum() {
        return EventHandlerType.CREATE;
    }

    private String type;
}
