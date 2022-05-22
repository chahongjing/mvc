package com.zjy.common.shiro;

import com.zjy.entity.model.UserInfo;

import java.io.Serializable;
import java.util.List;

public interface IUserService {

    UserInfo getByCode(String code);

    List<String> queryRoleCodeListByUserId(Long id);

    List<String> getPermissionListByUserId(Long id);
}
