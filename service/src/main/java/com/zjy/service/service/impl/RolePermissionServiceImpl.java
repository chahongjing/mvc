package com.zjy.service.service.impl;

import com.zjy.dao.RolePermissionDao;
import com.zjy.dao.vo.*;
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

import java.util.*;

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
    public List<PermissionVo> queryRolePermission(Long roleId) {
        if (roleId == null || roleId == 0) return new ArrayList<>();
        return dao.queryByRoleIdList(Collections.singletonList(roleId));
    }

    @Override
    public List<PermissionVo> queryRolePermission(List<Long> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) return new ArrayList<>();
        return dao.queryByRoleIdList(roleIdList);
    }

    @Override
    public List<PermissionCheckVo> getRolePermission(Long id) {
        List<PermissionCheckVo> allPermissionTree = permissionSrv.getAllPermissionTree();
        List<PermissionCheckVo> allPermissionList = new ArrayList<>();
        flatTree(allPermissionTree, allPermissionList);
        List<PermissionVo> rolePermissionVos = this.queryRolePermission(id);

        Set<Long> permissionSet = new HashSet<>();
        rolePermissionVos.forEach(item -> permissionSet.add(item.getId()));
        for (PermissionCheckVo permissionCheckVo : allPermissionList) {
            permissionCheckVo.setRelatedId(id);
            permissionCheckVo.setIsCheck(permissionSet.contains(permissionCheckVo.getId()));
        }
        return allPermissionTree;
    }

    @Override
    public void savePermission(List<PermissionCheckVo> list) {
        if (CollectionUtils.isEmpty(list)) return;
        RolePermission rp = new RolePermission();
        for (PermissionCheckVo item : list) {
            rp.setRoleId(item.getRelatedId());
            rp.setPermissionId(item.getId());
            if (item.getIsCheck()) {
                dao.insert(rp);
            } else {
                dao.deleteEntity(rp);
            }
        }
    }

    @Override
    public int deleteByPermission(Long permissionId){
        return dao.deleteByPermission(permissionId);
    }

    public void flatTree(List<PermissionCheckVo> list, List<PermissionCheckVo> result) {
        for (PermissionCheckVo permissionCheckVo : list) {
            result.add(permissionCheckVo);
            if(CollectionUtils.isEmpty(permissionCheckVo.getSubList())) continue;
            flatTree(permissionCheckVo.getSubList(), result);
        }
    }
}
