package com.zjy.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.zjy.dao.UpgradeLogDao;
import com.zjy.dao.vo.UpgradeLogItem;
import com.zjy.dao.vo.UpgradeLogVo;
import com.zjy.entity.model.UpgradeLog;
import com.zjy.service.common.BaseServiceImpl;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.UpgradeLogRequest;
import com.zjy.service.service.UpgradeLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UpgradeLogServiceImpl extends BaseServiceImpl<UpgradeLogDao, UpgradeLog> implements UpgradeLogService {

    @Override
    public UpgradeLog get(Long id) {
        UpgradeLog upgradeLog = super.get(id);
        return entityToVo(upgradeLog);
    }
    /**
     * 保存用户
     *
     * @param config
     */
    @Transactional
    public void save(UpgradeLog config) {
        UpgradeLog voDb = this.get(config.getId());
//        beforeCheck(config);
        // 处理密码
        if (voDb != null) {
            update(config);
        } else {
            config.setCreateTime(new Date());
            insert(config);
        }
    }

    public PageBean<UpgradeLogVo> queryPageList(UpgradeLogRequest request) {
        UpgradeLog po = new UpgradeLog();
        po.setTitle(request.getTitle());
        PageBean<UpgradeLogVo> upgradeLogPageBean = (PageBean<UpgradeLogVo>) super.queryPageListBase(request, po);
        for (UpgradeLogVo upgradeLogVo : upgradeLogPageBean.getList()) {
            convertContent(upgradeLogVo);
        }
        return upgradeLogPageBean;
    }

    @Override
    public List<UpgradeLogVo> queryList(UpgradeLog log) {
        List<UpgradeLogVo> upgradeLogVos = (List<UpgradeLogVo>) super.queryListBase(log);
        for (UpgradeLogVo upgradeLogVo : upgradeLogVos) {
            convertContent(upgradeLogVo);
        }
        return upgradeLogVos;
    }

    private UpgradeLogVo entityToVo(UpgradeLog log) {
        if(log == null) return null;
        UpgradeLogVo vo = new UpgradeLogVo();
        vo.setId(log.getId());
        vo.setUpgradeTime(log.getUpgradeTime());
        vo.setCreateTime(log.getCreateTime());
        vo.setTitle(log.getTitle());
        vo.setContent(log.getContent());
        vo.setContentList(convertToLogItem(vo.getContent()));
        return vo;
    }

    private List<UpgradeLogItem> convertToLogItem(String content) {
        if (StringUtils.isNotBlank(content)) {
            return JSON.parseArray(content, UpgradeLogItem.class);
        }
        return new ArrayList<>();
    }

    private UpgradeLogVo convertContent(UpgradeLogVo vo) {
        vo.setContentList(convertToLogItem(vo.getContent()));
        return vo;
    }
}
