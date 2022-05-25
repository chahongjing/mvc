package com.zjy.web.contoller;

import com.alibaba.fastjson.JSON;
import com.zjy.baseframework.enums.BaseResult;
import com.zjy.dao.vo.RelateCheckVo;
import com.zjy.service.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
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
    @RequiresPermissions("userRole_enter")
    public BaseResult<List<RelateCheckVo>> queryUserRole(Long id) {
        List<RelateCheckVo> list = userRoleSrv.queryAllRoleWithUserRole(id);
        return BaseResult.ok(list);
    }

    @RequestMapping("saveUserRole")
    @RequiresPermissions("userRole_enter")
    public BaseResult saveUserRole(String listStr) {
        List<RelateCheckVo> list = JSON.parseArray(listStr, RelateCheckVo.class);
        userRoleSrv.saveUserRole(list);
        return BaseResult.ok();
    }
}
