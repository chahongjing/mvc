package com.zjy.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zjy.dao.KvConfigLogDao;
import com.zjy.dao.vo.KvConfigLogVo;
import com.zjy.dao.vo.UserInfoVo;
import com.zjy.entity.model.KvConfig;
import com.zjy.entity.model.KvConfigLog;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.common.BaseService;
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
public class KvConfigLogServiceImpl extends BaseService<KvConfigLogDao, KvConfigLog> implements KvConfigLogService {
    @Autowired
    private UserInfoService userInfoSrv;
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
        LambdaQueryWrapper<KvConfigLog> query = Wrappers.lambdaQuery();
        query.eq(KvConfigLog::getKvId, request.getKvId());
        query.orderByDesc(KvConfigLog::getCreateTime);
        PageBean<KvConfigLogVo> pageBean = (PageBean<KvConfigLogVo>)super.queryPageList(request, query);
        for (KvConfigLogVo kvConfigLogVo : pageBean.getList()) {
            UserInfoVo user = userInfoSrv.getVo(kvConfigLogVo.getCreateBy());
            if(user != null) {
                kvConfigLogVo.setCreateByName(user.getName());
            }
        }
        return pageBean;
    }
}
