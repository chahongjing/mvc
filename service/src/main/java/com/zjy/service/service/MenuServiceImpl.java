package com.zjy.service.service;

import com.zjy.dao.MenuDao;
import com.zjy.dao.vo.MenuVo;
import com.zjy.dao.vo.RolePermissionVo;
import com.zjy.dao.vo.UserRoleVo;
import com.zjy.entity.model.Menu;
import com.zjy.service.common.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MenuServiceImpl extends BaseService<MenuDao, Menu> implements MenuService {
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Override
    public List<MenuVo> queryPermissionMenu() {
        Long userId = getCurrentUser().getId();
        List<UserRoleVo> roleList = userRoleService.queryListByUserId(userId);
        List<Long> roleIdList = roleList.stream().map(UserRoleVo::getRoleId).collect(Collectors.toList());
        List<MenuVo> result = new ArrayList<>();
        List<MenuVo> list = dao.query(null);
        List<MenuVo> parentList = list.stream().filter(item -> item.getPId() == null).collect(Collectors.toList());
        List<MenuVo> children = list.stream().filter(item -> item.getPId() != null && item.getPId() > 0).collect(Collectors.toList());
        List<RolePermissionVo> rolePermissionVos = rolePermissionService.queryRolePermission(roleIdList);
        for (MenuVo parent : parentList) {
            if (rolePermissionVos.stream().noneMatch(item -> roleIdList.contains(item.getRoleId()) && parent.getMenuId().equals(item.getPermissionId()))) {
                continue;
            }
            result.add(parent);
            List<MenuVo> temp = children.stream().filter(item -> item.getPId().equals(parent.getMenuId())
                    && rolePermissionVos.stream().anyMatch(innerItem -> roleIdList.contains(innerItem.getRoleId()) && item.getMenuId().equals(innerItem.getPermissionId()))
            ).collect(Collectors.toList());
            result.addAll(temp);
        }
        return result;
    }
}
