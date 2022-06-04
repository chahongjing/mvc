package com.zjy.service.service.impl;

import com.zjy.baseframework.common.ServiceException;
import com.zjy.dao.RoleInfoDao;
import com.zjy.dao.vo.RoleInfoVo;
import com.zjy.entity.model.RoleInfo;
import com.zjy.service.common.BaseServiceImpl;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.RoleInfoRequest;
import com.zjy.service.service.RoleInfoService;
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
public class RoleInfoServiceImpl extends BaseServiceImpl<RoleInfoDao, RoleInfo> implements RoleInfoService {

    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    public List<RoleInfoVo> queryAllRole() {
        return dao.queryAllRole();
    }

    /**
     * 保存用户
     *
     * @param vo
     */
    @Override
    @Transactional
    public void save(RoleInfoVo vo) {
        RoleInfoVo voDb = getVo(vo.getId());
        beforeCheck(vo);
        // 处理密码
        if (voDb.getIsSave()) {
            update(vo);
        } else {
            insert(vo);
        }
    }

    @Override
    public int delete(Long id) {
        // 删除rolepermission关系
        rolePermissionService.deleteByRole(id);
        return super.delete(id);
    }

    @Override
    public PageBean<RoleInfoVo> queryPageList(RoleInfoRequest request) {
        RoleInfo po = new RoleInfo();
        po.setName(request.getName());
        return (PageBean<RoleInfoVo>) super.queryPage(request, po);
    }

    @Override
    public RoleInfoVo getVo(Long id) {
        RoleInfo dbRole = get(id);
        RoleInfoVo vo = new RoleInfoVo();
        if (dbRole == null) {
            vo.setId(id);
            vo.setIsSave(false);
        } else {
            vo.setId(dbRole.getId());
            vo.setName(dbRole.getName());
            vo.setCode(dbRole.getCode());
            vo.setSeq(dbRole.getSeq());
            vo.setIsSave(true);
        }
        return vo;
    }

    protected void beforeCheck(RoleInfoVo po) {
        if (StringUtils.isBlank(po.getName())) {
            throw new ServiceException("请输入功能名称！");
        }
        Long id = po.getId() == null ? 0L : po.getId();
        Map<String, Integer> map = dao.queryRepeatCount(id, po.getCode());
        if (map != null && map.containsKey("codeCount") && map.get("codeCount").intValue() > 0) {
            throw new ServiceException("名称重复！");
        }
    }
}
