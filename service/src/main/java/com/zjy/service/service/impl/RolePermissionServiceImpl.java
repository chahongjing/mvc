package com.zjy.service.service.impl;

import com.zjy.dao.RolePermissionDao;
import com.zjy.dao.vo.RolePermissionVo;
import com.zjy.entity.model.RolePermission;
import com.zjy.service.common.BaseService;
import com.zjy.service.service.RolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class RolePermissionServiceImpl extends BaseService<RolePermissionDao, RolePermission> implements RolePermissionService {
    @Override
    public List<RolePermissionVo> queryRolePermission(List<Long> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) return Collections.emptyList();
        return dao.queryByRoleIdList(roleIdList);
    }
}
