package com.zjy.service.service.impl;
import com.zjy.baseframework.common.ServiceException;
import com.zjy.dao.PermissionDao;
import com.zjy.dao.vo.FunctionInfoVo;
import com.zjy.dao.vo.MenuVo;
import com.zjy.dao.vo.PermissionVo;
import com.zjy.dao.vo.PermissionCheckVo;
import com.zjy.entity.enums.PermissionType;
import com.zjy.entity.model.Permission;
import com.zjy.service.common.BaseServiceImpl;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.PermissionRequest;
import com.zjy.service.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionDao, Permission> implements PermissionService {
    @Lazy
    @Autowired
    protected MenuService menuSrv;
    @Lazy
    @Autowired
    protected FunctionInfoService functionInfoSrv;
    @Lazy
    @Autowired
    protected PermissionService permissionSrv;
    @Lazy
    @Autowired
    protected RolePermissionService rolePermissionSrv;
    @Lazy
    @Autowired
    protected UserPermissionService userPermissionSrv;

    @Override
    public List<PermissionVo> queryAllPermissionList() {
        return dao.queryAllPermissionList();
    }

    /**
     * 保存用户
     *
     * @param vo
     */
    @Override
    @Transactional
    public void save(PermissionVo vo) {
        PermissionVo voDb = getVo(vo.getId());
        beforeCheck(vo);
        // 处理密码
        if (voDb.getIsSave()) {
            update(vo);
        } else {
            insert(vo);
        }
    }

    @Override
    public int delete(Long id) {
        // 删除角色权限关系
        rolePermissionSrv.deleteByPermission(id);
        // 删除用户权限关系
        userPermissionSrv.deleteByPermission(id);
        return super.delete(id);
    }

    @Override
    public PageBean<? extends Permission> queryPageList(PermissionRequest request) {
        Permission po = new Permission();
        po.setName(request.getName());
        po.setFunctionId(request.getFunctionId());
        return super.queryPageListBase(request, po);
    }

    @Override
    public PermissionVo getVo(Long id) {
        Permission db = get(id);
        PermissionVo vo = new PermissionVo();
        if (db == null) {
            vo = new PermissionVo();
            vo.setId(id);
            vo.setIsSave(false);
        } else {
            vo.setId(db.getId());
            vo.setName(db.getName());
            vo.setCode(db.getCode());
            vo.setFunctionId(db.getFunctionId());
            vo.setSeq(db.getSeq());
            vo.setIsSave(true);
        }
        return vo;
    }

    @Override
    public void beforeCheck(Permission po) {
        if (StringUtils.isBlank(po.getName())) {
            throw new ServiceException("请输入功能名称！");
        }
        Long id = po.getId() == null ? 0L : po.getId();
        Map<String, Integer> map = dao.queryRepeatCount(id, po.getCode());
        if (map != null && map.containsKey("codeCount") && map.get("codeCount") > 0) {
            throw new ServiceException("权限编码重复！");
        }
    }

    public PermissionVo queryByTarget(Long targetId, PermissionType type) {
        if(targetId == null && !EnumSet.of(PermissionType.Menu, PermissionType.FunctionItem).contains(type)) {
            return null;
        }
        return dao.queryByTarget(targetId, type);
    }

    /**
     * 获取菜单，功能，权限树
     * @return
     */
    public List<PermissionCheckVo> getAllPermissionTree() {
        List<PermissionCheckVo> list = new ArrayList<>();
        // 获取所有一级菜单和二级菜单
        List<MenuVo> menuVos = menuSrv.queryAllMenu();
        // 获取菜单下所有页面
        List<FunctionInfoVo> functionInfoVos = functionInfoSrv.queryAllFunctionList();
        // 获取页面下所有权限
        List<PermissionVo> permissionVos = permissionSrv.queryAllPermissionList();
        // 组织数据
        PermissionCheckVo firtMenu;
        PermissionCheckVo secondMenu;
        PermissionCheckVo functionItem;
        PermissionCheckVo permissionItem;
        PermissionVo temp;
        // 一级
        for (MenuVo menuVo : menuVos) {
            if (menuVo.getPid() != null) continue;
            firtMenu = new PermissionCheckVo();
            // menu对应的permissionId
            temp = permissionVos.stream().filter(item -> item.getType() == PermissionType.Menu && menuVo.getId().equals(item.getTargetId())).findFirst().get();
            firtMenu.setId(temp.getId());
            firtMenu.setName(menuVo.getName());
            firtMenu.setType(PermissionType.Menu);
            firtMenu.setCode(temp.getCode());
            firtMenu.setShowDetail(true);
            list.add(firtMenu);
            // 二级
            List<MenuVo> children = menuVos.stream().filter(item -> menuVo.getId().equals(item.getPid())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(children)) continue;
            for (MenuVo child : children) {
                secondMenu = new PermissionCheckVo();
                // menu对应的permissionId
                temp = permissionVos.stream().filter(item -> item.getType() == PermissionType.Menu && child.getId().equals(item.getTargetId())).findFirst().get();
                secondMenu.setId(temp.getId());
                secondMenu.setName(child.getName());
                secondMenu.setType(PermissionType.Menu);
                secondMenu.setCode(temp.getCode());
                secondMenu.setShowDetail(true);
                firtMenu.getSubList().add(secondMenu);
                // 功能
                List<FunctionInfoVo> functions = functionInfoVos.stream().filter(item -> child.getId().equals(item.getMenuId())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(functions)) continue;
                for (FunctionInfoVo function : functions) {
                    functionItem = new PermissionCheckVo();
                    // function对应的permissionId
                    temp = permissionVos.stream().filter(item -> item.getType() == PermissionType.FunctionItem && function.getId().equals(item.getTargetId())).findFirst().get();
                    functionItem.setId(temp.getId());
                    functionItem.setName(function.getName());
                    functionItem.setType(PermissionType.FunctionItem);
                    functionItem.setCode(temp.getCode());
                    functionItem.setShowDetail(true);
                    secondMenu.getSubList().add(functionItem);
                    // 权限
                    List<PermissionVo> permissions = permissionVos.stream().filter(item -> function.getId().equals(item.getFunctionId()) && !item.getFunctionId().equals(item.getTargetId())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(permissions)) continue;
                    for (PermissionVo permission : permissions) {
                        permissionItem = new PermissionCheckVo();
                        permissionItem.setId(permission.getId());
                        permissionItem.setName(permission.getName());
                        permissionItem.setType(PermissionType.Permission);
                        permissionItem.setCode(permission.getCode());
                        permissionItem.setShowDetail(true);
                        functionItem.getSubList().add(permissionItem);
                    }
                }
            }
        }
        return list;
    }
}
