package com.zjy.dao;

import com.zjy.dao.common.BaseDao;
import com.zjy.dao.vo.FunctionInfoVo;
import com.zjy.entity.model.FunctionInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface FunctionInfoDao extends BaseDao<FunctionInfo> {
    List<FunctionInfoVo> queryAllFunctionList();
    Map<String, Integer> queryRepeatCount(@Param("id") Long id, @Param("code") String code);

    List<FunctionInfoVo> queryFunctionList();
}
