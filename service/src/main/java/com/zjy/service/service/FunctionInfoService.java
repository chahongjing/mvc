package com.zjy.service.service;

import com.zjy.dao.vo.FunctionInfoVo;
import com.zjy.entity.model.FunctionInfo;
import com.zjy.service.common.PageBean;
import com.zjy.service.component.BaseService;
import com.zjy.service.request.FunctionInfoRequest;

import java.util.List;

public interface FunctionInfoService extends BaseService<FunctionInfo> {
    List<FunctionInfoVo> queryAllFunctionList();

    FunctionInfoVo getVo(Long id);

    PageBean<? extends FunctionInfo> queryPageList(FunctionInfoRequest request);

    List<FunctionInfoVo> queryFunctionList();

    void save(FunctionInfoVo vo);
}
