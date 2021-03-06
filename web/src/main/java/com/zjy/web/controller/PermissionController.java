package com.zjy.web.controller;

import com.zjy.baseframework.enums.BaseResult;
import com.zjy.dao.vo.FunctionInfoVo;
import com.zjy.dao.vo.PermissionVo;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.PermissionRequest;
import com.zjy.service.service.FunctionInfoService;
import com.zjy.service.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/permission")
public class PermissionController extends BaseController {
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private FunctionInfoService functionInfoService;

    @RequestMapping("queryPageList")
    @RequiresPermissions("permissionList")
    public BaseResult<PageBean<PermissionVo>> queryPageList(PermissionRequest request) {
        PageBean<PermissionVo> pageBean = (PageBean<PermissionVo>) permissionService.queryPageList(request);
        return BaseResult.ok(pageBean);
    }

    @RequestMapping("getDetail")
    @RequiresPermissions("permissionEdit")
    public BaseResult<PermissionVo> getDetail(Long id, Long functionId) {
        PermissionVo permissionVo = permissionService.getVo(id);
        if (!permissionVo.getIsSave()) {
            permissionVo.setFunctionId(functionId);
            FunctionInfoVo functionInfo = functionInfoService.getVo(functionId);
            permissionVo.setFunctionName(functionInfo.getName());
        }
        return BaseResult.ok(permissionVo);
    }

    @PostMapping("save")
    @RequiresPermissions("permissionEdit_save")
    public BaseResult<String> save(PermissionVo vo) {
        permissionService.save(vo);
        return BaseResult.ok();
    }

    @RequestMapping("delete")
    @RequiresPermissions("permissionList_delete")
    public BaseResult<String> delete(Long id) {
        permissionService.delete(id);
        return BaseResult.ok();
    }
}
