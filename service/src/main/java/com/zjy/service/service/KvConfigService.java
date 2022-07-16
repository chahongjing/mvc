package com.zjy.service.service;

import com.zjy.entity.model.KvConfig;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.common.PageBean;
import com.zjy.service.component.BaseService;
import com.zjy.service.request.KvConfigRequest;

public interface KvConfigService extends BaseService<KvConfig> {
    KvConfig get(Long id);

    int insert(KvConfig config, UserInfo user);

    int update(KvConfig config, UserInfo user);

    int delete(Long id, UserInfo user);

    void save(KvConfig config, UserInfo user);

    PageBean<KvConfig> queryPageList(KvConfigRequest request);

    void removeAllCache();

    KvConfig getFromCache(String code);

    KvConfig getByCode(String code);
}
