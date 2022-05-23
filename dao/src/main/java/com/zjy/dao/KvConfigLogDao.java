package com.zjy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjy.entity.model.KvConfigLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface KvConfigLogDao extends BaseMapper<KvConfigLog> {
    List<KvConfigLog> queryByCode(String code);
    List<KvConfigLog> query(KvConfigLog entity);
}
