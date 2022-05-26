package com.zjy.service.service.impl;

import com.zjy.dao.RolePermissionDao;
import com.zjy.dao.vo.*;
import com.zjy.entity.enums.PermissionType;
import com.zjy.entity.model.RolePermission;
import com.zjy.service.common.BaseServiceImpl;
import com.zjy.service.service.FunctionInfoService;
import com.zjy.service.service.MenuService;
import com.zjy.service.service.PermissionService;
import com.zjy.service.service.RolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RolePermissionServiceImpl extends BaseServiceImpl<RolePermissionDao, RolePermission> implements RolePermissionService {

    @Lazy
    @Autowired
    protected MenuService menuSrv;

    @Autowired
    protected FunctionInfoService functionInfoSrv;

    @Autowired
    protected PermissionService permissionSrv;

    @Override
    public List<RolePermissionVo> queryRolePermission(Long roleId) {
        if (roleId == null || roleId == 0) return Collections.emptyList();
        return dao.queryByRoleIdList(Collections.singletonList(roleId));
    }

    @Override
    public List<RolePermissionVo> queryRolePermission(List<Long> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) return Collections.emptyList();
        return dao.queryByRoleIdList(roleIdList);
    }

    @Override
    public List<RelateCheckVo> getRolePermission(Long id) {
        List<RelateCheckVo> list = new ArrayList<>();
        // 获取所有一级菜单和二级菜单
        List<MenuVo> menuVos = menuSrv.queryAllMenu();
        // 获取菜单下所有页面
        List<FunctionInfoVo> functionInfoVos = functionInfoSrv.queryAllFunctionList();
        // 获取页面下所有权限
        List<PermissionVo> permissionVos = permissionSrv.queryAllPermissionList();
        // 获取角色下的权限
        List<RolePermissionVo> rolePermissionDbList = this.queryRolePermission(id);
        // 组织数据
        RelateCheckVo firtMenu;
        RelateCheckVo secondMenu;
        RelateCheckVo functionItem;
        RelateCheckVo permissionItem;
        // 一级
        for (MenuVo menuVo : menuVos) {
            if (menuVo.getPid() != null) continue;
            firtMenu = new RelateCheckVo();
            firtMenu.setId(id);
            firtMenu.setRelativeId(menuVo.getId());
            firtMenu.setName(menuVo.getName());
            firtMenu.setType(PermissionType.FirstMenu);
            if (rolePermissionDbList.stream().anyMatch(item -> item.getRoleId().equals(id) && item.getPermissionId().equals(menuVo.getId()))) {
                firtMenu.setSingleCheck(true);
            } else {
                firtMenu.setSingleCheck(false);
            }
            if (CollectionUtils.isEmpty(list) || true) firtMenu.setShowDetail(true);
            list.add(firtMenu);
            // 二级
            List<MenuVo> children = menuVos.stream().filter(item -> menuVo.getId().equals(item.getPid())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(children)) continue;
            for (MenuVo child : children) {
                secondMenu = new RelateCheckVo();
                secondMenu.setId(id);
                secondMenu.setRelativeId(child.getId());
                secondMenu.setName(child.getName());
                secondMenu.setType(PermissionType.SecondMenu);
                if (rolePermissionDbList.stream().anyMatch(item -> item.getRoleId().equals(id) && item.getPermissionId().equals(child.getId()))) {
                    secondMenu.setSingleCheck(true);
                } else {
                    secondMenu.setSingleCheck(false);
                }
                if (CollectionUtils.isEmpty(firtMenu.getSubList()) || true) secondMenu.setShowDetail(true);
                firtMenu.getSubList().add(secondMenu);
                // 功能
                List<FunctionInfoVo> functions = functionInfoVos.stream().filter(item -> child.getId().equals(item.getMenuId())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(functions)) continue;
                for (FunctionInfoVo function : functions) {
                    functionItem = new RelateCheckVo();
                    functionItem.setId(id);
                    functionItem.setRelativeId(function.getId());
                    functionItem.setName(function.getName());
                    functionItem.setType(PermissionType.FunctionItem);
                    if (rolePermissionDbList.stream().anyMatch(item -> item.getRoleId().equals(id) && item.getPermissionId().equals(function.getId()))) {
                        functionItem.setSingleCheck(true);
                    } else {
                        functionItem.setSingleCheck(false);
                    }
                    if (CollectionUtils.isEmpty(secondMenu.getSubList()) || true) functionItem.setShowDetail(true);
                    secondMenu.getSubList().add(functionItem);
                    // 权限
                    List<PermissionVo> permissions = permissionVos.stream().filter(item -> function.getId().equals(item.getFunctionId())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(permissions)) continue;
                    for (PermissionVo permission : permissions) {
                        permissionItem = new RelateCheckVo();
                        permissionItem.setId(id);
                        permissionItem.setRelativeId(permission.getId());
                        permissionItem.setName(permission.getName());
                        permissionItem.setType(PermissionType.Permission);
                        if (rolePermissionDbList.stream().anyMatch(item -> item.getRoleId().equals(id) && item.getPermissionId().equals(permission.getId()))) {
                            permissionItem.setIsCheck(true);
                        } else {
                            permissionItem.setIsCheck(false);
                        }
                        if (CollectionUtils.isEmpty(functionItem.getSubList()) || true) permissionItem.setShowDetail(true);
                        functionItem.getSubList().add(permissionItem);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public void savePermission(List<RelateCheckVo> list) {
        if (CollectionUtils.isEmpty(list)) return;
        EnumSet<PermissionType> set = EnumSet.of(PermissionType.FirstMenu, PermissionType.SecondMenu, PermissionType.FunctionItem);
        RolePermission rp = new RolePermission();
        for (RelateCheckVo item : list) {
            rp.setRoleId(item.getId());
            rp.setPermissionId(item.getRelativeId());
            if (set.contains(item.getType()) && item.getSingleCheck()) {
                dao.insert(rp);
            } else if (item.getType() == PermissionType.Permission && item.getIsCheck()) {
                dao.insert(rp);
            } else {
                dao.deleteEntity(rp);
            }
        }
    }
}
