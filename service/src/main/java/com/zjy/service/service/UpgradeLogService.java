package com.zjy.service.service;

import com.zjy.dao.vo.UpgradeLogVo;
import com.zjy.entity.model.UpgradeLog;
import com.zjy.service.component.BaseService;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.UpgradeLogRequest;

import java.util.List;

public interface UpgradeLogService extends BaseService<UpgradeLog> {
    void save(UpgradeLog config);

    PageBean<UpgradeLogVo> queryPageList(UpgradeLogRequest request);

    List<UpgradeLogVo> queryList(UpgradeLog log);
}
