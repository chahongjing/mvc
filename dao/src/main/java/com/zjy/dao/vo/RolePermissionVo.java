package com.zjy.dao.vo;

import com.zjy.entity.enums.PermissionType;
import com.zjy.entity.model.RolePermission;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolePermissionVo extends RolePermission {
    private String permissionCode;
    private Long targetId;
    private PermissionType type;
}
