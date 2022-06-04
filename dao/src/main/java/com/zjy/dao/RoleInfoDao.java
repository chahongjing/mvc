package com.zjy.dao;

import com.zjy.dao.common.BaseDao;
import com.zjy.dao.vo.RoleInfoVo;
import com.zjy.entity.model.RoleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoleInfoDao extends BaseDao<RoleInfo> {
    List<RoleInfoVo> queryAllRole();
    Map<String, Integer> queryRepeatCount(@Param("id") Long id, @Param("code") String code);
}
