package com.zjy.entity.model;

import com.zjy.entity.enums.PermissionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Permission {
    private Long id;
    private Long functionId;
    private String name;
    private String code;
    private Long targetId;
    private PermissionType type;
    private int seq;
}
