package com.zjy.web.contoller;

import com.alibaba.fastjson.JSON;
import com.zjy.baseframework.enums.BaseResult;
import com.zjy.dao.vo.PermissionCheckVo;
import com.zjy.service.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/userRole")
public class UserRoleController extends BaseController{
    @Autowired
    private UserRoleService userRoleSrv;

    @RequestMapping("queryUserRole")
    @RequiresPermissions("userRole")
    public BaseResult<List<PermissionCheckVo>> queryUserRole(Long id) {
        List<PermissionCheckVo> list = userRoleSrv.queryAllRoleWithUserRole(id);
        return BaseResult.ok(list);
    }

    @RequestMapping("saveUserRole")
    @RequiresPermissions("userRole")
    public BaseResult saveUserRole(String listStr) {
        List<PermissionCheckVo> list = JSON.parseArray(listStr, PermissionCheckVo.class);
        userRoleSrv.saveUserRole(list);
        return BaseResult.ok();
    }

    @RequestMapping("getUserPermission")
    @RequiresPermissions("userGrantPermission")
    public BaseResult getUserPermission(String listStr) {
        List<PermissionCheckVo> list = JSON.parseArray(listStr, PermissionCheckVo.class);
        userRoleSrv.saveUserRole(list);
        return BaseResult.ok();
    }
    @PostMapping("saveUserPermission")
    @RequiresPermissions("userGrantPermission")
    public BaseResult saveUserPermission(String listStr) {
        List<PermissionCheckVo> list = JSON.parseArray(listStr, PermissionCheckVo.class);
//        rolePermissionService.savePermission(list);
        return BaseResult.ok();
    }
}
