package com.zjy.dao;

import com.zjy.dao.common.BaseDao;
import com.zjy.dao.vo.UserRoleVo;
import com.zjy.entity.model.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleDao extends BaseDao<UserRole> {
    List<UserRoleVo> queryListByUserId(UserRoleVo vo);
    int deleteEntity(UserRole entity);
}
