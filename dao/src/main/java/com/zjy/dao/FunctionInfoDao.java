package com.zjy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjy.dao.vo.FunctionInfoVo;
import com.zjy.entity.model.FunctionInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface FunctionInfoDao extends BaseMapper<FunctionInfo> {
}
