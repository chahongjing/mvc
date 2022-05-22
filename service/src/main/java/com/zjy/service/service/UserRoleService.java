package com.zjy.service.service;

import com.zjy.dao.vo.UserRoleVo;

import java.io.Serializable;
import java.util.List;

public interface UserRoleService {
    List<UserRoleVo> queryListByUserId(Serializable userId);
}
