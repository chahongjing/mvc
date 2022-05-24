package com.zjy.service.service;

import com.zjy.dao.vo.OperateLogVo;
import com.zjy.entity.model.OperateLog;
import com.zjy.service.common.BaseService;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.OperateLogRequest;

import java.util.List;

public interface OperateLogService extends BaseService<OperateLog> {
    OperateLogVo getVo(Long id);
    int deleteAll();
    PageBean<OperateLogVo> queryPageList(OperateLogRequest request);
    List<OperateLogVo> queryList(OperateLogVo vo);
}
