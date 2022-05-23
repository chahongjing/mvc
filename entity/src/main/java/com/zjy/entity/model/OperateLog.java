package com.zjy.entity.model;

import com.zjy.entity.enums.LogLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OperateLog {
    private Long id;
    private Long userId;
    private String controller;
    private String method;
    private LogLevel logLevel;
    private String content;
    private Date createdOn;
}
