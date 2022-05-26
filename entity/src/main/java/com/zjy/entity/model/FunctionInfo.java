package com.zjy.entity.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FunctionInfo {
    private Long id;
    private Long menuId;
    private String name;
    private String code;
    private String path;
    private int seq;
}
