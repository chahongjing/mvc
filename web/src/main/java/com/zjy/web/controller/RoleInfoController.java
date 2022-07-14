package com.zjy.web.controller;

import com.zjy.baseframework.enums.BaseResult;
import com.zjy.dao.vo.PermissionCheckVo;
import com.zjy.dao.vo.RoleInfoVo;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.RoleInfoRequest;
import com.zjy.service.service.RoleInfoService;
import com.zjy.service.service.RolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/role")
public class RoleInfoController extends BaseController {
    @Autowired
    private RoleInfoService roleInfoService;
    @Autowired
    private RolePermissionService rolePermissionService;

    @RequestMapping("queryPageList")
    @RequiresPermissions("roleList")
    public BaseResult<PageBean> queryPageList(RoleInfoRequest request) {
        PageBean<RoleInfoVo> pageBean = roleInfoService.queryPageList(request);
        return BaseResult.ok(pageBean);
    }

    @RequestMapping("getDetail")
    @RequiresPermissions("roleEdit")
    public BaseResult<RoleInfoVo> getDetail(Long id) {
        RoleInfoVo userInfo = roleInfoService.getVo(id);
        return BaseResult.ok(userInfo);
    }

    @PostMapping("save")
    @RequiresPermissions("roleEdit_save")
    public BaseResult<String> save(RoleInfoVo vo) {
        roleInfoService.save(vo);
        return BaseResult.ok();
    }

    @RequestMapping("delete")
    @RequiresPermissions("roleList_delete")
    public BaseResult<String> delete(Long id) {
        roleInfoService.delete(id);
        return BaseResult.ok();
    }

    @RequestMapping("getRolePermission")
    @RequiresPermissions("grantPermission")
    public BaseResult<List<PermissionCheckVo>> getRolePermission(Long id) {
        List<PermissionCheckVo> list = rolePermissionService.getRolePermission(id);
        return BaseResult.ok(list);
    }

    @PostMapping("saveRolePermission")
    @RequiresPermissions("grantPermission")
    public BaseResult saveRolePermission(Long targetId, String listStr) {
        List<PermissionCheckVo> list = jsonUtils.parseList(listStr, PermissionCheckVo.class);
        rolePermissionService.savePermission(list);
        return BaseResult.ok();
    }
}
