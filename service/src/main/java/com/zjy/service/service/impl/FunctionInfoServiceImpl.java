package com.zjy.service.service.impl;

import com.zjy.baseframework.common.ServiceException;
import com.zjy.dao.FunctionInfoDao;
import com.zjy.dao.vo.FunctionInfoVo;
import com.zjy.dao.vo.PermissionVo;
import com.zjy.entity.enums.PermissionType;
import com.zjy.entity.model.FunctionInfo;
import com.zjy.entity.model.Permission;
import com.zjy.service.component.BaseServiceImpl;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.FunctionInfoRequest;
import com.zjy.service.service.FunctionInfoService;
import com.zjy.service.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FunctionInfoServiceImpl extends BaseServiceImpl<FunctionInfoDao, FunctionInfo> implements FunctionInfoService {
    @Autowired
    private PermissionService permissionService;

    @Override
    public List<FunctionInfoVo> queryAllFunctionList() {
        return dao.queryAllFunctionList();
    }

    @Override
    public FunctionInfoVo getVo(Long id) {
        FunctionInfo db = get(id);
        FunctionInfoVo vo = new FunctionInfoVo();
        if (db == null) {
            vo = new FunctionInfoVo();
            vo.setId(id);
            vo.setIsSave(false);
        } else {
            vo.setId(db.getId());
            vo.setName(db.getName());
            vo.setCode(db.getCode());
            vo.setMenuId(db.getMenuId());
            vo.setSeq(db.getSeq());
            vo.setPath(db.getPath());
            vo.setIsSave(true);
        }
        return vo;
    }

    @Override
    public PageBean<? extends FunctionInfo> queryPageList(FunctionInfoRequest request) {
        FunctionInfo po = new FunctionInfo();
        po.setName(request.getName());
        return super.queryPage(request, po);
    }

    @Override
    public List<FunctionInfoVo> queryFunctionList() {
        return dao.queryFunctionList();
    }

    @Override
    @Transactional
    public int delete(Long id) {
        // 删除权限点
        PermissionVo permissionVo = permissionService.queryByTarget(id, PermissionType.FunctionItem);
        if (permissionVo != null) {
            permissionService.delete(permissionVo.getId());
        }
        return super.delete(id);
    }

    /**
     * 保存用户
     *
     * @param vo
     */
    @Override
    @Transactional
    public void save(FunctionInfoVo vo) {
        FunctionInfoVo db = getVo(vo.getId());
        PermissionVo permission = permissionService.queryByTarget(vo.getId(), PermissionType.Menu);
        if (permission == null) {
            permission = new PermissionVo();
            permission.setSeq(1);
        }
        permission.setType(PermissionType.FunctionItem);
        permission.setName(vo.getName());
        permission.setCode(vo.getCode());
        beforeCheck(vo, permission);
        // 处理密码
        if (db.getIsSave()) {
            update(vo);
        } else {
            insert(vo);
            permission.setFunctionId(vo.getId());
            permission.setTargetId(vo.getId());
        }
        permissionService.save(permission);
    }

    protected void beforeCheck(FunctionInfoVo po, Permission permission) {
        if (StringUtils.isBlank(po.getName())) {
            throw new ServiceException("请输入功能名称！");
        }
        Long id = po.getId() == null ? 0L : po.getId();
        Map<String, Integer> map = dao.queryRepeatCount(id, po.getCode());
        if (map != null && map.containsKey("codeCount") && map.get("codeCount") > 0) {
            throw new ServiceException("功能编码重复！");
        }
        permissionService.beforeCheck(permission);
    }
}
