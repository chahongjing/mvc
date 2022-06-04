package com.zjy.service.service;

import com.zjy.baseframework.enums.BaseResult;
import com.zjy.common.shiro.IUserService;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.common.BaseService;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.UserInfoRequest;
import com.zjy.dao.vo.UserInfoVo;

import java.util.List;

/**
 * @author chahongjing
 * @create 2016-12-05 22:16
 */
public interface UserInfoService extends BaseService<UserInfo>, IUserService {
    int update(UserInfo po);

    int delete(Long id);

    void save(UserInfoVo vo);

    UserInfo get(Long id);

    UserInfoVo getVo(Long id);

    PageBean<? extends UserInfo> queryPageList(UserInfoRequest request);

    BaseResult<UserInfoVo> login(UserInfo user);

    BaseResult<String> logout();

    List<UserInfo> query(UserInfo user);

    UserInfoVo getVoByCode(String userCode);

    void changePassword(String userCode, String oldPassword, String newPassword);

    void resetPassword(String code, String password);
}
