package com.zjy.service.service;

import com.zjy.dao.vo.RoleInfoVo;
import com.zjy.entity.model.RoleInfo;
import com.zjy.service.common.BaseService;

import java.util.List;

public interface RoleInfoService extends BaseService<RoleInfo> {
    List<RoleInfoVo> queryAllRole();
}
