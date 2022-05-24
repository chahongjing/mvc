package com.zjy.dao;

import com.zjy.dao.common.BaseDao;
import com.zjy.dao.common.sql.SqlLog;
import com.zjy.dao.vo.UserInfoVo;
import com.zjy.entity.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserInfoDao extends BaseDao<UserInfo> {
    UserInfoVo getByCode(String userCode);

    Map<String, BigDecimal> queryRepeatCount(@Param("id") Long id, @Param("code") String code);

    int updateUserPassword(@Param("userId") Long userId, @Param("password") String password);

    @SqlLog
    List<UserInfo> getList(String id);
}
