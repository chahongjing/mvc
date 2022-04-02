package com.zjy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjy.dao.common.sql.SqlLog;
import com.zjy.entity.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserInfoDao extends BaseMapper<UserInfo> {
    UserInfo get(String id);
    @SqlLog
    List<UserInfo> getList(String id);
}
