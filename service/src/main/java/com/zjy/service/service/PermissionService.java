package com.zjy.service.service;

import com.zjy.dao.vo.PermissionVo;
import com.zjy.entity.model.Permission;
import com.zjy.service.common.BaseService;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.PermissionRequest;

import java.util.List;

public interface PermissionService extends BaseService<Permission> {
    List<PermissionVo> queryAllPermissionList();
    void save(PermissionVo vo);
    PageBean<? extends Permission> queryPageList(PermissionRequest request);
    PermissionVo getVo(Long id);
}
