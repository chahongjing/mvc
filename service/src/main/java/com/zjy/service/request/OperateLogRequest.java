package com.zjy.service.request;

import com.zjy.entity.enums.LogLevel;
import com.zjy.service.common.PageInfomation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperateLogRequest extends PageInfomation {
    private LogLevel logLevel;
}
