package com.zjy.common.shiro;

import java.util.List;

public interface IUserService {

    ShiroUserInfo getByCode(String code);

    List<String> queryRoleCodeListByUserId(Long id);

    List<String> getPermissionListByUserId(Long id);
}
