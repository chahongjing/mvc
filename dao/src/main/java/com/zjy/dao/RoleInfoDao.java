package com.zjy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjy.dao.vo.RoleInfoVo;
import com.zjy.entity.model.RoleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface RoleInfoDao extends BaseMapper<RoleInfo> {
}
