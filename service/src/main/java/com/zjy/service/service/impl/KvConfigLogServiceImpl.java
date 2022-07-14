package com.zjy.service.service.impl;

import com.zjy.dao.KvConfigLogDao;
import com.zjy.dao.vo.KvConfigLogVo;
import com.zjy.entity.model.KvConfig;
import com.zjy.entity.model.KvConfigLog;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.component.BaseServiceImpl;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.KvConfigLogRequest;
import com.zjy.service.service.KvConfigLogService;
import com.zjy.service.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
public class KvConfigLogServiceImpl extends BaseServiceImpl<KvConfigLogDao, KvConfigLog> implements KvConfigLogService {
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 添加用户
     *
     * @param config
     * @return
     */
    @Override
    @Transactional
    public int insert(KvConfig config, UserInfo user) {
        KvConfigLog log = new KvConfigLog();
        log.setKvId(config.getId());
        log.setCode(config.getCode());
        log.setValue(config.getValue());
        log.setCreateTime(new Date());
        log.setCreateBy(user.getId());
        int add = super.insert(log);
        return add;
    }

    /**
     * 添加用户
     *
     * @param config
     * @return
     */
    @Override
    @Transactional
    public int insert(KvConfigLog config) {
        int add = super.insert(config);
        return add;
    }

    @Override
    public PageBean<KvConfigLogVo> queryPageById(KvConfigLogRequest request) {
//        LambdaQueryWrapper<KvConfigLogVo> query = Wrappers.lambdaQuery();
//        query.eq(KvConfigLog::getKvId, request.getKvId());
//        query.orderByDesc(KvConfigLogVo::getCreateTime);
        KvConfigLog config = new KvConfigLog();
        config.setKvId(request.getKvId());
        request.setOrderBy("create_time desc");
        PageBean<KvConfigLogVo> pageBean = (PageBean<KvConfigLogVo>) super.queryPage(request, config);
        for (KvConfigLogVo kvConfigLogVo : pageBean.getList()) {
            UserInfo user = userInfoService.get(kvConfigLogVo.getCreateBy());
            if (user != null) {
                kvConfigLogVo.setCreateByName(user.getName());
            }
        }
        return pageBean;
    }
}
