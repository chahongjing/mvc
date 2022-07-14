package com.zjy.service.service.impl;

import com.zjy.dao.UserPermissionDao;
import com.zjy.dao.vo.*;
import com.zjy.entity.enums.PsermissionIncludeType;
import com.zjy.entity.model.Permission;
import com.zjy.entity.model.UserPermission;
import com.zjy.entity.model.UserRole;
import com.zjy.service.component.BaseServiceImpl;
import com.zjy.service.service.PermissionService;
import com.zjy.service.service.RolePermissionService;
import com.zjy.service.service.UserPermissionService;
import com.zjy.service.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserPermissionServiceImpl extends BaseServiceImpl<UserPermissionDao, UserPermission> implements UserPermissionService {
    @Autowired
    protected UserRoleService userRoleService;

    @Autowired
    protected RolePermissionService rolePermissionService;

    @Autowired
    protected PermissionService permissionService;

    /**
     * 获取用户权限表中的权限数据
     *
     * @param userId
     * @return
     */
    @Override
    public List<PermissionVo> queryUserPermission(Long userId) {
        if (userId == null) return new ArrayList<>();
        List<Long> userIdList = new ArrayList<>();
        userIdList.add(userId);
        return queryUserPermission(userIdList);
    }

    /**
     * 获取用户权限表中的权限数据
     *
     * @param userIdList
     * @return
     */
    @Override
    public List<PermissionVo> queryUserPermission(List<Long> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) return new ArrayList<>();
        return dao.queryByUserIdList(userIdList);
    }

    /**
     * 获取用户角色表中的权限数据
     *
     * @param userId
     * @return
     */
    @Override
    public List<PermissionVo> queryUserRolePermission(Long userId) {
        if (userId == null) return new ArrayList<>();
        List<Long> userIdList = new ArrayList<>();
        userIdList.add(userId);
        return queryUserRolePermission(userIdList);
    }

    /**
     * 获取用户角色表中的权限数据
     *
     * @param userIdList
     * @return
     */
    @Override
    public List<PermissionVo> queryUserRolePermission(List<Long> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) return new ArrayList<>();
        List<UserRoleVo> roleList = userRoleService.queryListByUserId(userIdList);
        List<Long> roleIdList = roleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        return rolePermissionService.queryRolePermission(roleIdList);
    }

    /**
     * 获取联合用户角色表，用户权限表后的用户权限数据
     *
     * @param userId
     * @return
     */
    @Override
    public List<PermissionVo> queryUserCombinePermission(Long userId) {
        if (userId == null) return new ArrayList<>();
        List<Long> userIdList = new ArrayList<>();
        userIdList.add(userId);
        Map<Long, List<PermissionVo>> map = queryUserCombinePermission(userIdList);
        return map.computeIfAbsent(userId, k -> new ArrayList<>());
    }

    /**
     * 获取联合用户角色表，用户权限表后的用户权限数据
     *
     * @param userIdList
     * @return
     */
    @Override
    public Map<Long, List<PermissionVo>> queryUserCombinePermission(List<Long> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) return new HashMap<>();
        List<UserRoleVo> roleList = userRoleService.queryListByUserId(userIdList);
        List<PermissionVo> rolePermissionList = queryUserRolePermission(userIdList);
        List<PermissionVo> userPermissionList = queryUserPermission(userIdList);

        List<PermissionVo> rolePermissionListTemp, userPermissionListTemp;
        Map<Long, List<PermissionVo>> result = new HashMap<>();
        for (Long userId : userIdList) {
            // 用户角色
            List<UserRoleVo> userRoleList = roleList.stream().filter(item -> userId.equals(item.getUserId())).collect(Collectors.toList());
            // 用户拥有的角色id
            List<Long> userRoleIdList = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            rolePermissionListTemp = rolePermissionList.stream().filter(item -> userRoleIdList.contains(item.getRoleId())).collect(Collectors.toList());
            userPermissionListTemp = userPermissionList.stream().filter(item -> userId.equals(item.getUserId())).collect(Collectors.toList());
            result.put(userId, handlePermission(rolePermissionListTemp, userPermissionListTemp));
        }
        return result;
    }

    private List<PermissionVo> handlePermission(List<PermissionVo> rolePermissionList, List<PermissionVo> userPermissionList) {
        // 角色权限全部包含
        List<PermissionVo> result = new ArrayList<>(rolePermissionList);
        List<PermissionVo> includeUserPermissionList = new ArrayList<>();
        Set<Long> excludeUserPermissionList = new HashSet<>();
        for (PermissionVo permissionVo : userPermissionList) {
            if (permissionVo.getIncludeType() == PsermissionIncludeType.INCLUDE) {
                includeUserPermissionList.add(permissionVo);
            } else if (permissionVo.getIncludeType() == PsermissionIncludeType.EXCLUDE) {
                excludeUserPermissionList.add(permissionVo.getId());
            }
        }
        // 用户权限包含的，全部返回
        result.addAll(includeUserPermissionList);
        // 再剔除排除的用户权限
        result.removeIf(item -> excludeUserPermissionList.contains(item.getId()));
        return result;
    }

    public List<PermissionCheckVo> getUserPermissionTree(Long userId) {
        List<PermissionVo> permissionVos = queryUserCombinePermission(userId);
        Set<Long> permissionSet = permissionVos.stream().map(PermissionVo::getId).collect(Collectors.toSet());

        List<PermissionCheckVo> allPermissionTree = permissionService.getAllPermissionTree();
        List<PermissionCheckVo> allPermissionList = new ArrayList<>();
        rolePermissionService.flatTree(allPermissionTree, allPermissionList);
        for (PermissionCheckVo permissionCheckVo : allPermissionList) {
            permissionCheckVo.setRelatedId(userId);
            permissionCheckVo.setIsCheck(permissionSet.contains(permissionCheckVo.getId()));
        }
        return allPermissionTree;
    }

    @Override
    public int deleteByPermission(Long permissionId) {
        return dao.deleteByPermission(permissionId);
    }

    @Override
    public void savePermission(Long userId, List<PermissionCheckVo> list) {
        if (CollectionUtils.isEmpty(list)) return;
        List<PermissionVo> userRolePermissionList = queryUserRolePermission(userId);
        Set<Long> permissionIdList = userRolePermissionList.stream().map(Permission::getId).collect(Collectors.toSet());
        UserPermission ur = new UserPermission();
        for (PermissionCheckVo item : list) {
            ur.setUserId(item.getRelatedId());
            ur.setPermissionId(item.getId());
            // 删除userpermission中的数据
            dao.deleteEntity(ur);
            if (item.getIsCheck() && !permissionIdList.contains(ur.getPermissionId())) {
                // role中没有，并插入
                ur.setIncludeType(PsermissionIncludeType.INCLUDE);
                dao.insert(ur);
            } else if (!item.getIsCheck() && permissionIdList.contains(ur.getPermissionId())) {
                // role中有，添加排除
                ur.setIncludeType(PsermissionIncludeType.EXCLUDE);
                dao.insert(ur);
            }
        }
    }
}
