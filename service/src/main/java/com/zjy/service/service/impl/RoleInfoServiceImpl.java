package com.zjy.service.service.impl;

import com.zjy.dao.RoleInfoDao;
import com.zjy.dao.vo.RoleInfoVo;
import com.zjy.entity.model.RoleInfo;
import com.zjy.service.common.BaseServiceImpl;
import com.zjy.service.service.RoleInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RoleInfoServiceImpl extends BaseServiceImpl<RoleInfoDao, RoleInfo> implements RoleInfoService {
    @Override
    public List<RoleInfoVo> queryAllRole() {
        return dao.queryAllRole();
    }
}
