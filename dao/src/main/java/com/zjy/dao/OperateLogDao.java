package com.zjy.dao;

import com.zjy.dao.common.BaseDao;
import com.zjy.dao.vo.OperateLogVo;
import com.zjy.entity.model.OperateLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperateLogDao extends BaseDao<OperateLog> {
    OperateLogVo getVo(Long id);
    int deleteAll();
}
