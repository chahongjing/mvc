package com.zjy.entity.model;

import com.zjy.entity.enums.PermissionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolePermission {
    private Long roleId;
    private Long permissionId;
    private PermissionType type;
}
