package com.zjy.web.contoller;

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
    private FunctionInfoService functionInfoSrv;

    @Autowired
    private MenuService menuSrv;

    @RequestMapping("queryPageList")
    @RequiresPermissions("functionList_enter")
    public BaseResult<PageBean<FunctionInfoVo>> queryPageList(FunctionInfoRequest request) {
        PageBean<FunctionInfoVo> pageBean = (PageBean<FunctionInfoVo>) functionInfoSrv.queryPageList(request);
        return BaseResult.ok(pageBean);
    }

    @RequestMapping("getDetail")
    @RequiresPermissions("functionEdit_enter")
    public BaseResult<FunctionInfoVo> getDetail(Long id, Long menuId) {
        FunctionInfoVo functionInfoVo = functionInfoSrv.getVo(id);
        if (!functionInfoVo.getIsSave()) {
            functionInfoVo.setMenuId(menuId);
            MenuVo menu = menuSrv.getVo(menuId);
            functionInfoVo.setMenuName(menu.getName());
        }
        return BaseResult.ok(functionInfoVo);
    }

    @PostMapping("save")
    @RequiresPermissions("functionEdit_save")
    public BaseResult<String> save(FunctionInfoVo vo) {
        functionInfoSrv.save(vo);
        return BaseResult.ok();
    }

    @RequestMapping("delete")
    @RequiresPermissions("functionList_delete")
    public BaseResult<String> delete(Long id) {
        functionInfoSrv.delete(id);
        return BaseResult.ok();
    }

    @RequestMapping("queryFunctionList")
    @RequiresPermissions("functionEdit_enter")
    public BaseResult<List<FunctionInfoVo>> queryFunctionList() {
        List<FunctionInfoVo> list = functionInfoSrv.queryFunctionList();
        return BaseResult.ok(list);
    }
}
