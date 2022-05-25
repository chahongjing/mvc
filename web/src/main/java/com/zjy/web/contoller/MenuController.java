package com.zjy.web.contoller;

import com.zjy.baseframework.enums.BaseResult;
import com.zjy.baseframework.model.TreeNode;
import com.zjy.dao.vo.MenuVo;
import com.zjy.dao.vo.UserInfoVo;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.MenuRequest;
import com.zjy.service.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {
    @Autowired
    private MenuService menuService;

    @RequestMapping("/queryMenu")
    public BaseResult<List<TreeNode>> queryMenu() {
        List<MenuVo> list = menuService.queryPermissionMenu();
        List<TreeNode> nodeList = new ArrayList<>();
        TreeNode node;
        for (MenuVo menu : list) {
            node = new TreeNode();
            node.setId(menu.getId());
            node.setPid(menu.getPid());
            node.setName(menu.getName());
            node.setSeq(menu.getSeq());
            node.setData(menu);
            nodeList.add(node);
        }

        return BaseResult.ok(nodeList);
    }

    @Autowired
    private MenuService menuSrv;

    @RequestMapping("queryPageList")
    @RequiresPermissions("menuList_enter")
    public BaseResult<PageBean> queryPageList(MenuRequest request) {
        PageBean<MenuVo> pageBean = menuSrv.queryPageList(request);
        return BaseResult.ok(pageBean);
    }

    @RequestMapping("getDetail")
    @RequiresPermissions("menuEdit_enter")
    public BaseResult<MenuVo> getDetail(Long id) {
        MenuVo userInfo = menuSrv.getVo(id);
        return BaseResult.ok(userInfo);
    }

    @PostMapping("save")
    @RequiresPermissions("menuEdit_save")
    public BaseResult<String> save(MenuVo vo) {
        menuSrv.save(vo);
        return BaseResult.ok();
    }

    @RequestMapping("delete")
    @RequiresPermissions("menuList_delete")
    public BaseResult<String> delete(Long id) {
        menuSrv.delete(id);
        return BaseResult.ok();
    }

    @RequestMapping("queryParentList")
    @RequiresPermissions("menuEdit_enter")
    public BaseResult<List<MenuVo>> queryParentList() {
        List<MenuVo> list = menuSrv.queryParentList();
        return BaseResult.ok(list);
    }

    @RequestMapping("queryPageMenuList")
    @RequiresPermissions("menuList_enter")
    public BaseResult<List<MenuVo>> queryPageList() {
        List<MenuVo> list = menuSrv.queryPageMenuList();
        return BaseResult.ok(list);
    }
}
