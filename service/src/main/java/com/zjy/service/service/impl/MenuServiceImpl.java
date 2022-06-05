package com.zjy.service.service.impl;

import com.zjy.baseframework.common.ServiceException;
import com.zjy.dao.MenuDao;
import com.zjy.dao.vo.MenuVo;
import com.zjy.dao.vo.PermissionVo;
import com.zjy.dao.vo.UserRoleVo;
import com.zjy.entity.enums.PermissionType;
import com.zjy.entity.model.Menu;
import com.zjy.entity.model.Permission;
import com.zjy.service.component.BaseServiceImpl;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.MenuRequest;
import com.zjy.service.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuDao, Menu> implements MenuService {
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private UserPermissionService userPermissionService;
    @Autowired
    private PermissionService permissionService;
    @Override
    public List<MenuVo> queryPermissionMenu() {
        Long userId = getCurrentUser().getId();
        List<UserRoleVo> roleList = userRoleService.queryListByUserId(userId);
        List<Long> roleIdList = roleList.stream().map(UserRoleVo::getRoleId).collect(Collectors.toList());
        List<MenuVo> result = new ArrayList<>();
        List<MenuVo> list = dao.query(null);
        List<MenuVo> parentList = list.stream().filter(item -> item.getPid() == null).collect(Collectors.toList());
        List<MenuVo> children = list.stream().filter(item -> item.getPid() != null && item.getPid() > 0).collect(Collectors.toList());
        List<PermissionVo> permissionVos = rolePermissionService.queryRolePermission(roleIdList);
        permissionVos.addAll(userPermissionService.queryUserPermission(userId));
        Set<Long> permissionMenuIdList = permissionVos.stream().filter(item -> item.getType() == PermissionType.Menu).map(Permission::getTargetId).collect(Collectors.toSet());
        for (MenuVo parent : parentList) {
            if (!permissionMenuIdList.contains(parent.getId())) {
                continue;
            }
            result.add(parent);
            List<MenuVo> temp = children.stream().filter(item -> item.getPid().equals(parent.getId())
                    && permissionMenuIdList.contains(item.getId())
            ).collect(Collectors.toList());
            result.addAll(temp);
        }
        return result;
    }


    /**
     * 添加用户
     *
     * @param entity
     * @return
     */
    @Transactional
    public int add(Menu entity) {
        return super.insert(entity);
    }

    /**
     * 修改用户
     *
     * @param entity
     * @return
     */
    @Override
    @Transactional
    public int update(Menu entity) {
        return super.update(entity);
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int delete(Long id) {
        // 删除权限点
        PermissionVo permissionVo = permissionService.queryByTarget(id, PermissionType.Menu);
        if(permissionVo != null) {
            permissionService.delete(permissionVo.getId());
        }
        return super.delete(id);
    }

    /**
     * 保存用户
     *
     * @param vo
     */
    @Override
    @Transactional
    public void save(MenuVo vo) {
        MenuVo voDb = getVo(vo.getId());
        PermissionVo permission = permissionService.queryByTarget(vo.getId(), PermissionType.Menu);
        if(permission == null) {
            permission = new PermissionVo();
            //        permission.setId();
//        permission.setFunctionId();
            permission.setSeq(0);
        }
        permission.setType(PermissionType.Menu);
        permission.setName(vo.getName());
        permission.setCode(vo.getCode());
        beforeCheck(vo, permission);
        // 处理密码
        if (voDb.getIsSave()) {
            update(vo);
        } else {
            add(vo);
            permission.setTargetId(vo.getId());
        }
        permissionService.save(permission);
    }

    @Override
    public PageBean<MenuVo> queryPageList(MenuRequest request) {
        Menu po = new Menu();
        po.setName(request.getName());
        return (PageBean<MenuVo>) super.queryPage(request, po);
    }

    @Override
    public MenuVo get(Long id) {
        MenuVo vo = null;
        Menu menu = super.get(id);
        if(menu != null) {
            vo = new MenuVo();
            vo.setId(menu.getId());
            vo.setName(menu.getName());
            vo.setPid(menu.getPid());
            vo.setCode(menu.getCode());
            vo.setUrl(menu.getUrl());
            vo.setIcon(menu.getIcon());
            vo.setSeq(menu.getSeq());
        }
        return vo;
    }

    @Override
    public MenuVo getVo(Long id) {
        MenuVo vo = get(id);
        if (vo == null) {
            vo = new MenuVo();
            vo.setId(id);
            vo.setIsSave(false);
        } else {
            vo.setIsSave(true);
        }
        return vo;
    }

    protected void beforeCheck(MenuVo po, Permission permission) {
        if (StringUtils.isBlank(po.getName())) {
            throw new ServiceException("请输入功能名称！");
        }
        Long id = po.getId() == null ? 0L : po.getId();
        Map<String, Integer> map = dao.queryRepeatCount(id, po.getCode());
        if (map != null && map.containsKey("codeCount") && map.get("codeCount").intValue() > 0) {
            throw new ServiceException("功能名称重复！");
        }
        permissionService.beforeCheck(permission);
    }

    public List<MenuVo> queryParentList() {
        return dao.queryParentList();
    }

    @Override
    public List<MenuVo> queryPageMenuList() {
        return dao.queryPageMenuList();
    }

    @Override
    public List<MenuVo> queryAllMenu() {
        List<MenuVo> result = new ArrayList<>();
        List<MenuVo> list = (List<MenuVo>) dao.query(null);
        List<MenuVo> parentList = list.stream().filter(item -> item.getPid() == null || item.getPid() == 0).collect(Collectors.toList());
        List<MenuVo> children = list.stream().filter(item -> item.getPid() != null && item.getPid() > 0).collect(Collectors.toList());
        for (MenuVo parent : parentList) {
            result.add(parent);
            List<MenuVo> temp = children.stream().filter(item -> item.getPid().equals(parent.getId())).collect(Collectors.toList());
            result.addAll(temp);
        }
        return result;
    }
}
