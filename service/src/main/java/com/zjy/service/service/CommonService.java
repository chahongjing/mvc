package com.zjy.service.service;

import com.zjy.service.enums.SwitchEnum;

public interface CommonService {
    String getEnums();

    boolean isSwitchOpen(SwitchEnum se);
}
