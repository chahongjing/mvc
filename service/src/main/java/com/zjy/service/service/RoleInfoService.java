package com.zjy.service.service;

import com.zjy.dao.vo.RoleInfoVo;
import com.zjy.entity.model.RoleInfo;
import com.zjy.service.component.BaseService;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.RoleInfoRequest;

import java.util.List;

public interface RoleInfoService extends BaseService<RoleInfo> {
    List<RoleInfoVo> queryAllRole();

    void save(RoleInfoVo vo);

    PageBean<RoleInfoVo> queryPageList(RoleInfoRequest request);

    RoleInfoVo getVo(Long id);

    int delete(Long id);
}
