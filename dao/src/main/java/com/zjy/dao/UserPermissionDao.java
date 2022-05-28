package com.zjy.dao;

import com.zjy.dao.common.BaseDao;
import com.zjy.dao.vo.PermissionVo;
import com.zjy.entity.model.UserPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserPermissionDao extends BaseDao<UserPermission> {
    List<PermissionVo> queryByUserIdList(List<Long> userIdList);
    int deleteEntity(UserPermission entity);
    int deleteByPermission(@Param("permissionId") Long permissionId);
}
