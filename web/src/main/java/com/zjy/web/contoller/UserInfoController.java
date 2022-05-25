package com.zjy.web.contoller;

import com.zjy.baseframework.enums.BaseResult;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.UserInfoRequest;
import com.zjy.service.service.UserInfoService;
import com.zjy.dao.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserInfoController extends BaseController {
    @Autowired
    private UserInfoService userInfoSrv;

    @RequestMapping("/login")
    public BaseResult<UserInfoVo> login(UserInfo user) {
        return userInfoSrv.login(user);
    }

    @RequestMapping(value = "/logout")
    public BaseResult<String> logout() {
        return userInfoSrv.logout();
    }

    // region 用户管理
    @RequestMapping("userEdit")
    @RequiresPermissions("userEdit_enter")
    public String editUser(String userId, Model model) {
        model.addAttribute("userId", userId);
        return "sys/userEdit";
    }

    @RequestMapping("getDetail")
    @ResponseBody
    @RequiresPermissions("userEdit_enter")
    public BaseResult<UserInfoVo> getDetail(Long id) {
        UserInfoVo userInfo = userInfoSrv.getVo(id);
        return BaseResult.ok(userInfo);
    }

    @PostMapping("save")
    @ResponseBody
    public BaseResult<String> save(UserInfoVo vo) {
        UserInfo currentUser = getCurrentUser();
        if (!(isPermitted("userEdit_save") || (currentUser != null && currentUser.getCode().equals(vo.getCode())))) {
            throw new UnauthorizedException();
        }
        userInfoSrv.save(vo);
        return BaseResult.ok();
    }

    @RequestMapping("delete")
    @ResponseBody
    @RequiresPermissions(value = {"userList_delete"}, logical = Logical.OR)
    public BaseResult<String> delete(Long id) {
        userInfoSrv.delete(id);
        return BaseResult.ok();
    }

    @RequestMapping("changePassword")
    @ResponseBody
    public BaseResult<String> changePassword(String code, String oldPassword, String newPassword) {
        userInfoSrv.changePassword(code, oldPassword, newPassword);
        return BaseResult.ok();
    }

    @RequestMapping("resetPassword")
    @ResponseBody
    @RequiresPermissions(value = {"userList_resetPassword"})
    public BaseResult<String> resetPassword(String code, String password) {
        userInfoSrv.resetPassword(code, password);
        return BaseResult.ok();
    }

    @RequestMapping("queryPageList")
    @ResponseBody
    @RequiresPermissions("userList_enter")
    public BaseResult<PageBean> queryPageList(UserInfoRequest request) {
        PageBean<UserInfoVo> pageBean = (PageBean<UserInfoVo>) userInfoSrv.queryPageList(request);
        return BaseResult.ok(pageBean);
//        throw new ServiceException("dfsfd");
    }
}
