package com.zjy.service.service.impl;

import com.zjy.baseframework.common.ServiceException;
import com.zjy.dao.FunctionInfoDao;
import com.zjy.dao.vo.FunctionInfoVo;
import com.zjy.entity.model.FunctionInfo;
import com.zjy.service.common.BaseServiceImpl;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.FunctionInfoRequest;
import com.zjy.service.service.FunctionInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FunctionInfoServiceImpl extends BaseServiceImpl<FunctionInfoDao, FunctionInfo> implements FunctionInfoService {
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
        return super.queryPageListBase(request, po);
    }
    @Override
    public List<FunctionInfoVo> queryFunctionList() {
        return dao.queryFunctionList();
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
        beforeCheck(vo);
        // 处理密码
        if (db.getIsSave()) {
            update(vo);
        } else {
            insert(vo);
        }
    }

    protected void beforeCheck(FunctionInfoVo po) {
        if (StringUtils.isBlank(po.getName())) {
            throw new ServiceException("请输入功能名称！");
        }
        Long id = po.getId() == null ? 0L : po.getId();
        Map<String, Integer> map = dao.queryRepeatCount(id, po.getCode());
        if (map != null && map.containsKey("codeCount") && map.get("codeCount") > 0) {
            throw new ServiceException("功能编码重复！");
        }
    }
}
