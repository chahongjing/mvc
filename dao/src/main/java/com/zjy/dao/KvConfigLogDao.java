package com.zjy.dao;

import com.zjy.dao.common.BaseDao;
import com.zjy.dao.vo.KvConfigLogVo;
import com.zjy.entity.model.KvConfigLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface KvConfigLogDao extends BaseDao<KvConfigLog> {
    List<KvConfigLogVo> queryByCode(String code);

    List<KvConfigLogVo> query(KvConfigLog entity);
}
