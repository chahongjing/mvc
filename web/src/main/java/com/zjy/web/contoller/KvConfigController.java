package com.zjy.web.contoller;

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
public class KvConfigController extends BaseController{
    //region 属性
    @Autowired
    private KvConfigService kvConfigSrv;
    @Autowired
    private KvConfigLogService kvConfigLogSrv;
    //endregion

    @RequestMapping("/queryPageList")
    @RequiresPermissions("kvConfig_enter")
    public BaseResult<PageBean> queryPageList(KvConfigRequest request) {
        PageBean<KvConfig> pageBean = kvConfigSrv.queryPageList(request);
        return BaseResult.ok(pageBean);
    }

    @RequestMapping("/getDetail")
    @RequiresPermissions("kvConfig_detail_enter")
    public BaseResult<KvConfig> getDetail(Long id) {
        KvConfig kvConfig = kvConfigSrv.get(id);
        return BaseResult.ok(kvConfig);
    }

    @PostMapping("/save")
    @RequiresPermissions("kvConfig_save")
    public BaseResult<String> save(KvConfig vo) {
        UserInfo currentUser = getCurrentUser();
        kvConfigSrv.save(vo, currentUser);
        return BaseResult.ok();
    }

    @RequestMapping("/delete")
    @RequiresPermissions("kvConfig_delete")
    public BaseResult<String> delete(Long id) {
        UserInfo currentUser = getCurrentUser();
        kvConfigSrv.delete(id, currentUser);
        return BaseResult.ok();
    }

    @RequestMapping("/removeAllCache")
    @RequiresPermissions("kvConfig_clear_cache")
    public BaseResult<String> removeAllCache() {
        kvConfigSrv.removeAllCache();
        return BaseResult.ok();
    }

    @RequestMapping("/queryLogPageList")
    public BaseResult<PageBean> queryPageList(KvConfigLogRequest request) {
        PageBean<KvConfigLogVo> pageBean = kvConfigLogSrv.queryPageById(request);
        return BaseResult.ok(pageBean);
    }
}
