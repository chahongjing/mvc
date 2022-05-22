package com.zjy.service.service;

import com.zjy.dao.vo.RolePermissionVo;

import java.util.List;

public interface RolePermissionService {
    List<RolePermissionVo> queryRolePermission(List<String> roleIdList);
}
