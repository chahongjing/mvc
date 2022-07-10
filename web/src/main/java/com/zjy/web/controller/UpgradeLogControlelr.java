package com.zjy.web.controller;

import com.zjy.baseframework.enums.BaseResult;
import com.zjy.dao.vo.UpgradeLogVo;
import com.zjy.entity.model.UpgradeLog;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.UpgradeLogRequest;
import com.zjy.service.service.UpgradeLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/upgradeLog")
public class UpgradeLogControlelr extends BaseController{
    //region 属性
    @Autowired
    private UpgradeLogService upgradeLogService;
    //endregion

    @RequestMapping("/queryPageList")
    public BaseResult<PageBean> queryPageList(UpgradeLogRequest request) {
        return BaseResult.ok(upgradeLogService.queryPageList(request));
    }

    @RequestMapping("/queryList")
    public BaseResult<List<UpgradeLogVo>> queryList(UpgradeLog log) {
        return BaseResult.ok(upgradeLogService.queryList(log));
    }

    @RequestMapping("/getDetail")
    public BaseResult<UpgradeLog> getDetail(Long id) {
        return BaseResult.ok(upgradeLogService.get(id));
    }

    @PostMapping("/save")
    public BaseResult<String> save(UpgradeLogVo vo) {
        if(CollectionUtils.isNotEmpty(vo.getContentList())) {
            vo.setContent(jsonUtils.toJSON(vo.getContentList()));
        }
        upgradeLogService.save(vo);
        return BaseResult.ok();
    }

    @RequestMapping("/delete")
    public BaseResult<String> delete(Long id) {
        upgradeLogService.delete(id);
        return BaseResult.ok();
    }
}
