package com.zjy.service.service;

import com.zjy.dao.vo.RelateCheckVo;
import com.zjy.dao.vo.RolePermissionVo;
import com.zjy.entity.model.RolePermission;
import com.zjy.service.common.BaseService;

import java.util.List;

public interface RolePermissionService extends BaseService<RolePermission> {
    List<RolePermissionVo> queryRolePermission(Long roleId);
    List<RolePermissionVo> queryRolePermission(List<Long> roleIdList);
    List<RelateCheckVo> getRolePermission(Long id);
    void savePermission(List<RelateCheckVo> list);
}
