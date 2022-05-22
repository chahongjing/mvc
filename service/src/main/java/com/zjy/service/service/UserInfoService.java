package com.zjy.service.service;

import com.zjy.baseframework.enums.BaseResult;
import com.zjy.common.shiro.IUserService;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.UserInfoRequest;
import com.zjy.dao.vo.UserInfoVo;

import java.util.List;

/**
 * @author chahongjing
 * @create 2016-12-05 22:16
 */
public interface UserInfoService extends IUserService {
    int add(UserInfo po);

    int update(UserInfo po);

    int delete(String id);

    void save(UserInfoVo vo);

    UserInfoVo getVo(String id);

    PageBean<? extends UserInfo> queryPageList(UserInfoRequest request);

    BaseResult<UserInfoVo> login(UserInfo user);

    BaseResult<String> logout();

    List<UserInfo> query(UserInfo user);

    UserInfoVo getByCode(String userCode);

    void changePassword(String userCode, String oldPassword, String newPassword);

    void resetPassword(String userCode, String password);
}
