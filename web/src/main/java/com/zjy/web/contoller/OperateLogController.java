package com.zjy.web.contoller;

import com.zjy.baseframework.annotations.LogMessage;
import com.zjy.baseframework.enums.BaseResult;
import com.zjy.dao.vo.OperateLogVo;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.OperateLogRequest;
import com.zjy.service.service.OperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/operateLog")
public class OperateLogController extends BaseController{
    @Autowired
    private OperateLogService operateLogService;

    @RequestMapping("queryPageList")
    @RequiresPermissions("operateLogList")
    @LogMessage(doLog = false)
    public BaseResult<PageBean> queryPageList(OperateLogRequest request) {
        PageBean<OperateLogVo> pageBean = operateLogService.queryPageList(request);
        return BaseResult.ok(pageBean);
    }

    @RequestMapping("getDetail")
    @RequiresPermissions("operateLogEdit")
    @LogMessage(doLog = false)
    public BaseResult<OperateLogVo> getDetail(Long id) {
        OperateLogVo operateLogVo = operateLogService.getVo(id);
        return BaseResult.ok(operateLogVo);
    }

    @RequestMapping("delete")
    @RequiresPermissions("operateLogList_delete")
    @LogMessage(doLog = false)
    public BaseResult<OperateLogVo> delete(Long id) {
        operateLogService.delete(id);
        return BaseResult.ok();
    }

    @RequestMapping("deleteAll")
    @LogMessage(doLog = false)
    public BaseResult<OperateLogVo> deleteAll() {
        operateLogService.deleteAll();
        return BaseResult.ok();
    }
}
