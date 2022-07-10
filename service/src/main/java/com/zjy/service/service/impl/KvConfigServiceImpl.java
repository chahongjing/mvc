package com.zjy.service.service.impl;

import com.zjy.baseframework.common.RedisKeyUtils;
import com.zjy.baseframework.interfaces.ICache;
import com.zjy.dao.KvConfigDao;
import com.zjy.entity.model.KvConfig;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.component.BaseServiceImpl;
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
import java.util.List;

@Slf4j
@Service
public class KvConfigServiceImpl extends BaseServiceImpl<KvConfigDao, KvConfig> implements KvConfigService {
    @Autowired
    private ICache cache;

    @Autowired
    private KvConfigLogService kvConfigLogService;

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
        cache.set(getKey(RedisKeyUtils.KV_CONFIG, config.getCode()), config.getValue());
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
        kvConfigLogService.insert(voDb, user);
        int update = super.update(config);
        cache.set(getKey(RedisKeyUtils.KV_CONFIG, config.getCode()), config.getValue());
        return update;
    }

    @Override
    public KvConfig get(Long id) {
        KvConfig kvConfig = super.get(id);
//        cache.get(getHKey(KEY, kvConfig.getCode()));
        return kvConfig;
    }

    @Override
    public KvConfig getFromCache(String code) {
        String o = (String)cache.get(getKey(RedisKeyUtils.KV_CONFIG, code));
        if(StringUtils.isNotBlank(o)) {
            return jsonUtils.parse(o, KvConfig.class);
        }
        KvConfig byCode = this.getByCode(code);
        if(byCode != null) {
            cache.set(getKey(RedisKeyUtils.KV_CONFIG, code), byCode.getValue());
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
            cache.delete(getKey(RedisKeyUtils.KV_CONFIG, kvConfig.getCode()));
            kvConfigLogService.insert(kvConfig, user);
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
        List<KvConfig> list = (List<KvConfig>)super.query(new KvConfig());
        for (KvConfig kvConfig : list) {
            cache.delete(getKey(RedisKeyUtils.KV_CONFIG, kvConfig.getCode()));
        }
    }

    @Override
    public PageBean<KvConfig> queryPageList(KvConfigRequest request) {
        KvConfig config = new KvConfig();
        config.setCode(request.getCode());
        return (PageBean<KvConfig>) super.queryPage(request, config);
    }

    private String getKey(String key, String field) {
        return String.format("%s:%s", key, field);
    }
}
