package com.zjy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjy.dao.vo.UserRoleVo;
import com.zjy.entity.model.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;

@Mapper
public interface UserRoleDao extends BaseMapper<UserRole> {
    List<UserRoleVo> queryListByUserId(UserRoleVo vo);
}
