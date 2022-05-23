package com.zjy.service.service.impl;

import com.zjy.dao.UpgradeLogDao;
import com.zjy.entity.model.UpgradeLog;
import com.zjy.service.common.BaseService;
import com.zjy.service.service.UpgradeLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UpgradeLogServiceImpl extends BaseService<UpgradeLogDao, UpgradeLog> implements UpgradeLogService {
}
