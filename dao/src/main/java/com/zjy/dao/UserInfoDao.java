package com.zjy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjy.dao.common.sql.SqlLog;
import com.zjy.dao.vo.UserInfoVo;
import com.zjy.entity.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserInfoDao extends BaseMapper<UserInfo> {
    UserInfoVo getByCode(String userCode);

    Map<String, BigDecimal> queryRepeatCount(@Param("userId") Long userId, @Param("userCode") String userCode);

    int updateUserPassword(@Param("userId") Long userId, @Param("password") String password);

    @SqlLog
    List<UserInfo> getList(String id);
}
