package com.zjy.rpc_provider.common;

import com.zjy.baseframework.common.ServiceException;
import com.zjy.baseframework.enums.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.filter.ExceptionFilter;

/**
 * @author junyi.zeng
 * @date 2019-11-20 09:06:12
 */
@Slf4j
@Activate(group = CommonConstants.PROVIDER)
public class CustomeExceptionFilter extends ExceptionFilter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            Result invoke = invoker.invoke(invocation);
            if (!invoke.hasException()) {
                return invoke;
            }
            return handleException(invoke.getException());
        } catch (Throwable throwable) {
            return handleException(throwable);
        }
    }

    protected AppResponse getDefaultRpcResult(Result result) {
        return new AppResponse(new RuntimeException(StringUtils.toString(result.getException())));
    }

    protected AppResponse handleException(Throwable throwable) {
        BaseResult result = BaseResult.ok();
        String msg = null;
        if (throwable instanceof ServiceException) {
            ServiceException bizException = ((ServiceException) throwable);
            msg = bizException.getMessage();
//            if(bizException.getEcode() != null && bizException.getEcode().getError() == ErrorLevel.ERROR){
//                try {
////                    Monitor.alarm(PoolConfig.MONITOR_KEY, "【客服工作台rpc】" + throwable.getMessage());
//                } catch(Exception e) {
//                    log.error("发送飞书报警异常！", e);
//                }
//                log.error(String.format("dubbo接口执行警告"), throwable);
//            }
//            else{
            log.warn(String.format("dubbo接口执行警告"), throwable);
//            }
        } else {
            msg = "工单系统异常！";
            log.error(String.format("dubbo接口执行出错"), throwable);
            try {
//                Monitor.alarm(PoolConfig.MONITOR_KEY, "【客服工作台rpc】" + throwable.getMessage());
            } catch (Exception e) {
                log.error("发送飞书报警异常！", e);
            }
        }
        return new AppResponse(BaseResult.error(throwable.getMessage()));
    }
}
