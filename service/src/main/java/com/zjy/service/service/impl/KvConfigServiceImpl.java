package com.zjy.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zjy.baseframework.interfaces.ICache;
import com.zjy.dao.KvConfigDao;
import com.zjy.entity.model.KvConfig;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.common.BaseService;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.KvConfigRequest;
import com.zjy.service.service.KvConfigLogService;
import com.zjy.service.service.KvConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
public class KvConfigServiceImpl extends BaseService<KvConfigDao, KvConfig> implements KvConfigService {
    @Autowired
    private ICache cache;
    private final static String KEY = "kvconfig";

    @Autowired
    private KvConfigLogService kvConfigLogSrv;

    /**
     * 添加用户
     *
     * @param config
     * @return
     */
    @Override
    @Transactional
    public int insert(KvConfig config, UserInfo user) {
        int add = super.insert(config);
        cache.set(getHKey(KEY, config.getCode()), config.getValue());
        return add;
    }

    /**
     * 修改用户
     *
     * @param config
     * @return
     */
    @Override
    @Transactional
    public int update(KvConfig config, UserInfo user) {
        KvConfig voDb = this.get(config.getId());
        kvConfigLogSrv.insert(voDb, user);
        int update = super.update(config);
        cache.set(getHKey(KEY, config.getCode()), config.getValue());
        return update;
    }

    @Override
    public KvConfig get(Long id) {
        KvConfig kvConfig = super.get(id);
//        cache.get(getHKey(KEY, kvConfig.getCode()));
        return kvConfig;
    }

    @Override
    public KvConfig getByCache(String code) {
        String o = (String)cache.get(getHKey(KEY, code));
        if(StringUtils.isNotBlank(o)) {
            return JSON.parseObject(o, KvConfig.class);
        }
        KvConfig byCode = this.getByCode(code);
        if(byCode != null) {
            cache.set(getHKey(KEY, code), byCode.getValue());
        }
        return byCode;
    }

    @Override
    public KvConfig getByCode(String code) {
        return dao.getByCode(code);
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int delete(Long id, UserInfo user) {
        KvConfig kvConfig = this.get(id);
        if(kvConfig != null) {
            int delete = super.delete(id);
            cache.delete(getHKey(KEY, kvConfig.getCode()));
            kvConfigLogSrv.insert(kvConfig, user);
            return delete;
        }
        return -1;
    }

    /**
     * 保存用户
     *
     * @param config
     */
    @Transactional
    public void save(KvConfig config, UserInfo user) {
        KvConfig voDb = this.get(config.getId());
//        beforeCheck(config);
        // 处理密码
        if (voDb != null) {
            update(config, user);
        } else {
            config.setCreateTime(new Date());
            insert(config, user);
        }
    }

    @Override
    public void removeAllCache() {
        cache.getAll(KEY).forEach((key, value) -> cache.delete(key));
    }

    @Override
    public PageBean<KvConfig> queryPageList(KvConfigRequest request) {
        return (PageBean<KvConfig>) super.queryPageList(request, Wrappers.query());
    }

    private String getHKey(String key, String field) {
        return String.format("%s:%s", key, field);
    }
}
