package com.zjy.web.controller;

import com.zjy.baseframework.enums.BaseResult;
import com.zjy.dao.vo.FunctionInfoVo;
import com.zjy.dao.vo.MenuVo;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.FunctionInfoRequest;
import com.zjy.service.service.FunctionInfoService;
import com.zjy.service.service.MenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/function")
public class FunctionInfoController extends BaseController {

    @Autowired
    private FunctionInfoService functionInfoService;

    @Autowired
    private MenuService menuService;

    @RequestMapping("queryPageList")
    @RequiresPermissions("functionList")
    public BaseResult<PageBean<FunctionInfoVo>> queryPageList(FunctionInfoRequest request) {
        PageBean<FunctionInfoVo> pageBean = (PageBean<FunctionInfoVo>) functionInfoService.queryPageList(request);
        return BaseResult.ok(pageBean);
    }

    @RequestMapping("getDetail")
    @RequiresPermissions("functionEdit")
    public BaseResult<FunctionInfoVo> getDetail(Long id, Long menuId) {
        FunctionInfoVo functionInfoVo = functionInfoService.getVo(id);
        if (!functionInfoVo.getIsSave()) {
            functionInfoVo.setMenuId(menuId);
            MenuVo menu = menuService.getVo(menuId);
            functionInfoVo.setMenuName(menu.getName());
        }
        return BaseResult.ok(functionInfoVo);
    }

    @PostMapping("save")
    @RequiresPermissions("functionEdit_save")
    public BaseResult<String> save(FunctionInfoVo vo) {
        functionInfoService.save(vo);
        return BaseResult.ok();
    }

    @RequestMapping("delete")
    @RequiresPermissions("functionList_delete")
    public BaseResult<String> delete(Long id) {
        functionInfoService.delete(id);
        return BaseResult.ok();
    }

    @RequestMapping("queryFunctionList")
    @RequiresPermissions("functionEdit")
    public BaseResult<List<FunctionInfoVo>> queryFunctionList() {
        List<FunctionInfoVo> list = functionInfoService.queryFunctionList();
        return BaseResult.ok(list);
    }
}
