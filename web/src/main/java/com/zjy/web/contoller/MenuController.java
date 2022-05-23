package com.zjy.web.contoller;

import com.zjy.baseframework.enums.BaseResult;
import com.zjy.baseframework.model.TreeNode;
import com.zjy.dao.vo.MenuVo;
import com.zjy.dao.vo.UserInfoVo;
import com.zjy.service.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
}
