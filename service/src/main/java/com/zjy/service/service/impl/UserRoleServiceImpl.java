package com.zjy.service.service.impl;

import com.zjy.baseframework.interfaces.ICache;
import com.zjy.dao.UserRoleDao;
import com.zjy.dao.vo.RelateCheckVo;
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

    public List<UserRoleVo> queryListByUserId(Long userId){
        UserRoleVo urv = new UserRoleVo();
        urv.setUserId(userId);
        return dao.queryListByUserId(urv);
    }

    @Autowired
    protected RoleInfoService roleInfoSrv;

    @Autowired
    private ICache cacheHelper;

    @Override
    public List<RelateCheckVo> queryAllRoleWithUserRole(Long userId) {
        List<RelateCheckVo> list = new ArrayList<>();
        RelateCheckVo root;
        RelateCheckVo role;
        root = new RelateCheckVo();
        root.setName("角色列表");
        if (CollectionUtils.isEmpty(list)) root.setShowDetail(true);
        list.add(root);
        List<RoleInfoVo> roleInfoVos = roleInfoSrv.queryAllRole();
        List<UserRoleVo> userRoleList = this.queryListByUserId(userId);
        for (RoleInfoVo roleInfoVo : roleInfoVos) {
            role = new RelateCheckVo();
            role.setId(userId);
            role.setRelativeId(roleInfoVo.getId());
            role.setName(roleInfoVo.getName());
            if (userRoleList.stream().anyMatch(item -> item.getRoleId().equals(roleInfoVo.getId()))) {
                role.setIsCheck(true);
            }
            root.getSubList().add(role);
        }
        return list;
    }

    @Override
    public void saveUserRole(List<RelateCheckVo> list) {
        if (CollectionUtils.isEmpty(list)) return;
        UserRole ur = new UserRole();
        for (RelateCheckVo item : list) {
            ur.setUserId(item.getId());
            ur.setRoleId(item.getRelativeId());
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
