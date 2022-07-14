package com.zjy.web.controller;

import com.zjy.baseframework.enums.BaseResult;
import com.zjy.dao.vo.KvConfigLogVo;
import com.zjy.entity.model.KvConfig;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.KvConfigLogRequest;
import com.zjy.service.request.KvConfigRequest;
import com.zjy.service.service.KvConfigLogService;
import com.zjy.service.service.KvConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/kvConfig")
public class KvConfigController extends BaseController {
    //region 属性
    @Autowired
    private KvConfigService kvConfigService;
    @Autowired
    private KvConfigLogService kvConfigLogService;
    //endregion

    @RequestMapping("/queryPageList")
    @RequiresPermissions("kvConfig")
    public BaseResult<PageBean> queryPageList(KvConfigRequest request) {
        PageBean<KvConfig> pageBean = kvConfigService.queryPageList(request);
        return BaseResult.ok(pageBean);
    }

    @RequestMapping("/getDetail")
    @RequiresPermissions("kvConfigDetail")
    public BaseResult<KvConfig> getDetail(Long id) {
        KvConfig kvConfig = kvConfigService.get(id);
        return BaseResult.ok(kvConfig);
    }

    @PostMapping("/save")
    @RequiresPermissions("kvConfig_save")
    public BaseResult<String> save(KvConfig vo) {
        UserInfo currentUser = getCurrentUser();
        kvConfigService.save(vo, currentUser);
        return BaseResult.ok();
    }

    @RequestMapping("/delete")
    @RequiresPermissions("kvConfig_delete")
    public BaseResult<String> delete(Long id) {
        UserInfo currentUser = getCurrentUser();
        kvConfigService.delete(id, currentUser);
        return BaseResult.ok();
    }

    @RequestMapping("/removeAllCache")
    @RequiresPermissions("kvConfig_clear_cache")
    public BaseResult<String> removeAllCache() {
        kvConfigService.removeAllCache();
        return BaseResult.ok();
    }

    @RequestMapping("/queryLogPageList")
    public BaseResult<PageBean> queryPageList(KvConfigLogRequest request) {
        PageBean<KvConfigLogVo> pageBean = kvConfigLogService.queryPageById(request);
        return BaseResult.ok(pageBean);
    }
}
