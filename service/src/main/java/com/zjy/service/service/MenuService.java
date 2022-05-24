package com.zjy.service.service;

import com.zjy.dao.vo.MenuVo;
import com.zjy.entity.model.Menu;
import com.zjy.service.common.BaseService;

import java.util.List;

public interface MenuService extends BaseService<Menu> {
    List<MenuVo> queryPermissionMenu();
}
