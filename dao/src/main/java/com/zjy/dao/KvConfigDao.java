package com.zjy.dao;

import com.zjy.dao.common.BaseDao;
import com.zjy.dao.vo.KvConfigVo;
import com.zjy.entity.model.KvConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface KvConfigDao extends BaseDao<KvConfig> {
    KvConfig getByCode(String code);

    List<KvConfigVo> query(KvConfig entity);
}
