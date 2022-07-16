package com.zjy.service.service;

import com.zjy.dao.vo.MenuVo;
import com.zjy.entity.model.Menu;
import com.zjy.service.common.PageBean;
import com.zjy.service.component.BaseService;
import com.zjy.service.request.MenuRequest;

import java.util.List;

public interface MenuService extends BaseService<Menu> {
    MenuVo getVo(Long id);

    PageBean<MenuVo> queryPageList(MenuRequest request);

    void save(MenuVo vo);

    List<MenuVo> queryParentList();

    List<MenuVo> queryPageMenuList();

    List<MenuVo> queryPermissionMenu();

    List<MenuVo> queryAllMenu();
}
