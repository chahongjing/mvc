package com.zjy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjy.dao.vo.UserRoleVo;
import com.zjy.entity.model.UserRole;

import java.io.Serializable;
import java.util.List;

public interface UserRoleDao extends BaseMapper<UserRole> {
    List<UserRoleVo> queryListByUserId(Serializable userId);
}
