package com.zjy.dao.vo;

import com.zjy.entity.enums.PermissionType;
import com.zjy.entity.model.UserPermission;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPermissionVo extends UserPermission {
    private String permissionCode;
    private Long targetId;
    private PermissionType type;
}
