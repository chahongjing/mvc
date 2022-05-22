package com.zjy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjy.dao.vo.RolePermissionVo;
import com.zjy.entity.model.RolePermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RolePermissionDao extends BaseMapper<RolePermission> {
    List<RolePermissionVo> queryByRoleIdList(List<Long> roleIdList);
}
