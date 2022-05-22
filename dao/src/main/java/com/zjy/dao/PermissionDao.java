package com.zjy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjy.dao.vo.PermissionVo;
import com.zjy.entity.model.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PermissionDao extends BaseMapper<Permission> {
    Map<String, Integer> queryRepeatCount(@Param("permissionId") String permissionId, @Param("code") String code);

    List<PermissionVo> queryAllPermissionList();
}
