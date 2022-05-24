package com.zjy.service.service;

import com.zjy.dao.vo.UserRoleVo;
import com.zjy.entity.model.UserRole;
import com.zjy.service.common.BaseService;

import java.io.Serializable;
import java.util.List;

public interface UserRoleService extends BaseService<UserRole> {
    List<UserRoleVo> queryListByUserId(Long userId);
}
