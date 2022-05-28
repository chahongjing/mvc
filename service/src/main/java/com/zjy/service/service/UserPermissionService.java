package com.zjy.service.service;

import com.zjy.dao.vo.PermissionCheckVo;
import com.zjy.dao.vo.PermissionVo;
import com.zjy.entity.model.UserPermission;
import com.zjy.service.common.BaseService;

import java.util.List;
import java.util.Map;

public interface UserPermissionService extends BaseService<UserPermission> {
    List<PermissionCheckVo> getUserPermissionTree(Long userId);
    void savePermission(Long userId, List<PermissionCheckVo> list);
    List<PermissionVo> queryUserPermission(Long userId);
    List<PermissionVo> queryUserPermission(List<Long> userIdList);
    List<PermissionVo> queryUserRolePermission(Long userId);
    List<PermissionVo> queryUserRolePermission(List<Long> userIdList);
    List<PermissionVo> queryUserCombinePermission(Long userId);
    Map<Long, List<PermissionVo>>  queryUserCombinePermission(List<Long> userIdList);
    int deleteByPermission(Long permissionId);
}
