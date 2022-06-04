package com.zjy.service.service;

import com.zjy.dao.vo.PermissionCheckVo;
import com.zjy.dao.vo.PermissionVo;
import com.zjy.entity.model.RolePermission;
import com.zjy.service.common.BaseService;

import java.util.List;

public interface RolePermissionService extends BaseService<RolePermission> {
    List<PermissionVo> queryRolePermission(Long roleId);
    List<PermissionVo> queryRolePermission(List<Long> roleIdList);
    List<PermissionCheckVo> getRolePermission(Long id);
    void savePermission(List<PermissionCheckVo> list);
    void flatTree(List<PermissionCheckVo> list, List<PermissionCheckVo> result);
    int deleteByPermission(Long permissionId);
    int deleteByRole(Long roleId);
}
