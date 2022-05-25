package com.zjy.service.service.impl;

import com.zjy.baseframework.common.ServiceException;
import com.zjy.dao.MenuDao;
import com.zjy.dao.vo.MenuVo;
import com.zjy.dao.vo.RolePermissionVo;
import com.zjy.dao.vo.UserRoleVo;
import com.zjy.entity.model.Menu;
import com.zjy.service.common.BaseServiceImpl;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.MenuRequest;
import com.zjy.service.service.MenuService;
import com.zjy.service.service.RoleInfoService;
import com.zjy.service.service.RolePermissionService;
import com.zjy.service.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuDao, Menu> implements MenuService {
    @Autowired
    protected RoleInfoService roleInfoSrv;
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
        List<MenuVo> parentList = list.stream().filter(item -> item.getPid() == null).collect(Collectors.toList());
        List<MenuVo> children = list.stream().filter(item -> item.getPid() != null && item.getPid() > 0).collect(Collectors.toList());
        List<RolePermissionVo> rolePermissionVos = rolePermissionService.queryRolePermission(roleIdList);
        for (MenuVo parent : parentList) {
            if (rolePermissionVos.stream().noneMatch(item -> roleIdList.contains(item.getRoleId()) && parent.getId().equals(item.getPermissionId()))) {
                continue;
            }
            result.add(parent);
            List<MenuVo> temp = children.stream().filter(item -> item.getPid().equals(parent.getId())
                    && rolePermissionVos.stream().anyMatch(innerItem -> roleIdList.contains(innerItem.getRoleId()) && item.getId().equals(innerItem.getPermissionId()))
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
        beforeCheck(vo);
        // 处理密码
        if (voDb.getIsSave()) {
            update(vo);
        } else {
            add(vo);
        }
    }

    @Override
    public PageBean<MenuVo> queryPageList(MenuRequest request) {
        Menu po = new Menu();
        po.setName(request.getName());
        return (PageBean<MenuVo>) super.queryPageListBase(request, po);
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

    protected void beforeCheck(MenuVo po) {
        if (StringUtils.isBlank(po.getName())) {
            throw new ServiceException("请输入功能名称！");
        }
        Map<String, BigDecimal> map = dao.queryRepeatCount(po.getId(), po.getCode());
        if (map != null && map.containsKey("CODECOUNT") && map.get("CODECOUNT").intValue() > 0) {
            throw new ServiceException("功能名称重复！");
        }
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
        List<MenuVo> parentList = list.stream().filter(item -> item.getPid() != null && item.getPid() > 0).collect(Collectors.toList());
        List<MenuVo> children = list.stream().filter(item -> item.getPid() != null && item.getPid() > 0).collect(Collectors.toList());
        for (MenuVo parent : parentList) {
            result.add(parent);
            List<MenuVo> temp = children.stream().filter(item -> item.getPid().equals(parent.getId())).collect(Collectors.toList());
            result.addAll(temp);
        }
        return result;
    }
}
