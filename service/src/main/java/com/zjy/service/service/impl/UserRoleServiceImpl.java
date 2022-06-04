package com.zjy.service.service.impl;

import com.zjy.baseframework.interfaces.ICache;
import com.zjy.dao.UserRoleDao;
import com.zjy.dao.vo.PermissionCheckVo;
import com.zjy.dao.vo.RoleInfoVo;
import com.zjy.dao.vo.UserRoleVo;
import com.zjy.entity.model.UserRole;
import com.zjy.service.common.BaseServiceImpl;
import com.zjy.service.service.RoleInfoService;
import com.zjy.service.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleDao, UserRole> implements UserRoleService {

    @Autowired
    protected RoleInfoService roleInfoService;

    @Autowired
    private ICache cacheHelper;

    @Override
    public List<UserRoleVo> queryListByUserId(Long userId){
        if(userId == null) return new ArrayList<>();
        List<Long> idList = new ArrayList<>();
        idList.add(userId);
        return queryListByUserId(idList);
    }
    @Override
    public List<UserRoleVo> queryListByUserId(List<Long> userIdList){
        if(CollectionUtils.isEmpty(userIdList)) return new ArrayList<>();
        UserRoleVo urv = new UserRoleVo();
        urv.setUserIdList(userIdList);
        return dao.queryListByUserId(urv);
    }

    @Override
    public List<PermissionCheckVo> queryAllRoleWithUserRole(Long userId) {
        List<PermissionCheckVo> list = new ArrayList<>();
        PermissionCheckVo root;
        PermissionCheckVo role;
        root = new PermissionCheckVo();
        root.setName("角色列表");
        if (CollectionUtils.isEmpty(list)) root.setShowDetail(true);
        list.add(root);
        List<RoleInfoVo> roleInfoVos = roleInfoService.queryAllRole();
        List<UserRoleVo> userRoleList = this.queryListByUserId(userId);
        for (RoleInfoVo roleInfoVo : roleInfoVos) {
            role = new PermissionCheckVo();
            role.setId(userId);
//            role.setRelativeId(roleInfoVo.getId());
            role.setName(roleInfoVo.getName());
            if (userRoleList.stream().anyMatch(item -> item.getRoleId().equals(roleInfoVo.getId()))) {
                role.setIsCheck(true);
            }
            root.getSubList().add(role);
        }
        return list;
    }

    @Override
    public void saveUserRole(List<PermissionCheckVo> list) {
        if (CollectionUtils.isEmpty(list)) return;
        UserRole ur = new UserRole();
        for (PermissionCheckVo item : list) {
            ur.setUserId(item.getId());
//            ur.setRoleId(item.getRelativeId());
            if (item.getIsCheck()) {
                dao.insert(ur);
            } else {
                dao.deleteEntity(ur);
            }
        }
    }

    @Override
    public List<String> queryUserRoleCodeList(Long userId) {
        List<UserRoleVo> userRoleList = queryListByUserId(userId);
        return userRoleList.stream().map(UserRoleVo::getRoleCode).distinct().collect(Collectors.toList());
    }


}
