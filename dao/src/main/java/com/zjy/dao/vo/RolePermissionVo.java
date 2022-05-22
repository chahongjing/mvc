package com.zjy.dao.vo;

import com.zjy.entity.model.RolePermission;

public class RolePermissionVo extends RolePermission {
    private String roleCode;
    private String permissionCode;

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }
}
