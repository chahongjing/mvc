package com.zjy.service.service;

import com.zjy.dao.vo.KvConfigLogVo;
import com.zjy.entity.model.KvConfig;
import com.zjy.entity.model.KvConfigLog;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.common.BaseService;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.KvConfigLogRequest;

public interface KvConfigLogService extends BaseService<KvConfigLog> {
    int insert(KvConfig config, UserInfo user);

    PageBean<KvConfigLogVo> queryPageById(KvConfigLogRequest request);
}
