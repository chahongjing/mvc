package com.zjy.entity.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Permission {
    private Long id;
    private Long functionId;
    private String name;
    private String code;
    private int seq;
}
