package com.zjy.dao;

import com.zjy.dao.common.BaseDao;
import com.zjy.entity.model.Permission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionDao extends BaseDao<Permission> {
}
