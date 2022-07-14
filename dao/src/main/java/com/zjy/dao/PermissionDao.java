package com.zjy.dao;

import com.zjy.dao.common.BaseDao;
import com.zjy.dao.vo.PermissionVo;
import com.zjy.entity.enums.PermissionType;
import com.zjy.entity.model.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PermissionDao extends BaseDao<Permission> {
    List<PermissionVo> queryAllPermissionList();

    Map<String, Integer> queryRepeatCount(@Param("id") Long id, @Param("code") String code);

    PermissionVo queryByTarget(@Param("targetId") Long targetId, @Param("type") PermissionType type);
}
