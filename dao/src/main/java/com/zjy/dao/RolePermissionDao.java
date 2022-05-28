package com.zjy.dao;

import com.zjy.dao.common.BaseDao;
import com.zjy.dao.vo.PermissionVo;
import com.zjy.entity.model.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RolePermissionDao extends BaseDao<RolePermission> {
    List<PermissionVo> queryByRoleIdList(List<Long> roleIdList);
    int deleteEntity(RolePermission entity);
    int deleteByPermission(@Param("permissionId") Long permissionId);
    int deleteByRole(@Param("roleId") Long roleId);
}
