package com.zjy.service.service;

import com.zjy.dao.vo.PermissionCheckVo;
import com.zjy.dao.vo.UserRoleVo;
import com.zjy.entity.model.UserRole;
import com.zjy.service.component.BaseService;

import java.util.List;

public interface UserRoleService extends BaseService<UserRole> {
    List<UserRoleVo> queryListByUserId(Long userId);

    List<UserRoleVo> queryListByUserId(List<Long> userIdList);

    List<PermissionCheckVo> queryAllRoleWithUserRole(Long id);

    void saveUserRole(List<PermissionCheckVo> list);

    List<String> queryUserRoleCodeList(Long userId);
}
