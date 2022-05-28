package com.zjy.entity.model;

import com.zjy.entity.enums.PsermissionIncludeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPermission {
    private Long userId;
    private Long permissionId;
    private PsermissionIncludeType includeType;
}
