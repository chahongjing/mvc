package com.zjy.dao;

import com.zjy.dao.common.BaseDao;
import com.zjy.dao.vo.RoleInfoVo;
import com.zjy.entity.model.RoleInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleInfoDao extends BaseDao<RoleInfo> {
    List<RoleInfoVo> queryAllRole();
}
