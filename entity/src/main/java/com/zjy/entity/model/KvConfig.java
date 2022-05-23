package com.zjy.entity.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class KvConfig {
    private Long id;
    private String code;
    private String value;
    private String memo;
    private Date createTime;
}
