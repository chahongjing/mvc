package com.zjy.entity.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class KvConfigLog {
    private Long id;
    private String code;
    private String value;
    private Long createBy;
    private Long kvId;
    private Date createTime;
}
