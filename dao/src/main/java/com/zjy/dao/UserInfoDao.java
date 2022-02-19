package com.zjy.dao;

import com.zjy.dao.common.sql.SqlLog;
import com.zjy.entity.model.UserInfo;

import java.util.List;

public interface UserInfoDao {
    UserInfo get(String id);
    @SqlLog
    List<UserInfo> getList(String id);
}
