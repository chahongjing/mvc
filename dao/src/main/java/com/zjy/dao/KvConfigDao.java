package com.zjy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjy.dao.vo.KvConfigVo;
import com.zjy.entity.model.KvConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface KvConfigDao extends BaseMapper<KvConfig> {
    KvConfig getByCode(String code);
    List<KvConfigVo> query(KvConfig entity);
}
