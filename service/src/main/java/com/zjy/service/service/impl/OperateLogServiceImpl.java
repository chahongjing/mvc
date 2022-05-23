package com.zjy.service.service.impl;

import com.zjy.dao.OperateLogDao;
import com.zjy.entity.model.OperateLog;
import com.zjy.service.common.BaseService;
import com.zjy.service.service.OperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OperateLogServiceImpl extends BaseService<OperateLogDao, OperateLog> implements OperateLogService {
}
