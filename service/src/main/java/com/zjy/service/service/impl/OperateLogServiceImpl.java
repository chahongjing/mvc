package com.zjy.service.service.impl;

import com.zjy.dao.OperateLogDao;
import com.zjy.dao.vo.OperateLogVo;
import com.zjy.entity.model.OperateLog;
import com.zjy.service.component.BaseServiceImpl;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.OperateLogRequest;
import com.zjy.service.service.OperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OperateLogServiceImpl extends BaseServiceImpl<OperateLogDao, OperateLog> implements OperateLogService {
    @Override
    public OperateLogVo getVo(Long id) {
        return dao.getVo(id);
    }

    @Override
    public PageBean<OperateLogVo> queryPageList(OperateLogRequest request) {
        OperateLogVo po = new OperateLogVo();
        po.setLogLevel(request.getLogLevel());
        return (PageBean<OperateLogVo>) super.queryPage(request, po);
    }

    @Override
    public List<OperateLogVo> queryList(OperateLogVo vo) {
        return (List<OperateLogVo>) dao.query(vo);
    }

    /**
     * 删除
     *
     * @return
     */
    public int deleteAll() {
        return dao.deleteAll();
    }
}
