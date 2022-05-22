package com.zjy.service.service;

import com.zjy.dao.FunctionInfoDao;
import com.zjy.entity.model.FunctionInfo;
import com.zjy.service.common.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FunctionInfoServiceImpl extends BaseService<FunctionInfoDao, FunctionInfo> implements FunctionInfoService {


    @Autowired
    protected PermissionService permissionSrv;


}
