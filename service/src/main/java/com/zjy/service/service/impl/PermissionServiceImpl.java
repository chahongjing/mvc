package com.zjy.service.service.impl;
import com.zjy.baseframework.common.ServiceException;
import com.zjy.dao.PermissionDao;
import com.zjy.dao.vo.PermissionVo;
import com.zjy.entity.model.Permission;
import com.zjy.service.common.BaseServiceImpl;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.PermissionRequest;
import com.zjy.service.service.PermissionService;
import com.zjy.service.service.RolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionDao, Permission> implements PermissionService {

    @Override
    public List<PermissionVo> queryAllPermissionList() {
        return dao.queryAllPermissionList();
    }

    /**
     * 保存用户
     *
     * @param vo
     */
    @Override
    @Transactional
    public void save(PermissionVo vo) {
        PermissionVo voDb = getVo(vo.getId());
        beforeCheck(vo);
        // 处理密码
        if (voDb.getIsSave()) {
            update(vo);
        } else {
            insert(vo);
        }
    }

    @Override
    public PageBean<? extends Permission> queryPageList(PermissionRequest request) {
        Permission po = new Permission();
        po.setName(request.getName());
        po.setFunctionId(request.getFunctionId());
        return super.queryPageListBase(request, po);
    }

    @Override
    public PermissionVo getVo(Long id) {
        Permission db = get(id);
        PermissionVo vo = new PermissionVo();
        if (db == null) {
            vo = new PermissionVo();
            vo.setId(id);
            vo.setIsSave(false);
        } else {
            vo.setId(db.getId());
            vo.setName(db.getName());
            vo.setCode(db.getCode());
            vo.setFunctionId(db.getFunctionId());
            vo.setSeq(db.getSeq());
            vo.setIsSave(true);
        }
        return vo;
    }

    protected void beforeCheck(PermissionVo po) {
        if (StringUtils.isBlank(po.getName())) {
            throw new ServiceException("请输入功能名称！");
        }
        Long id = po.getId() == null ? 0L : po.getId();
        Map<String, Integer> map = dao.queryRepeatCount(id, po.getCode());
        if (map != null && map.containsKey("codeCount") && map.get("codeCount") > 0) {
            throw new ServiceException("权限编码重复！");
        }
    }
}
