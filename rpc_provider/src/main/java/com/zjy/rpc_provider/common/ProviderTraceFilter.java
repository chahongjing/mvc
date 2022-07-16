package com.zjy.rpc_provider.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * dubbo接口log调用链及dubbo出入参监控filter
 *
 * @author zhangjingjing
 */
@Slf4j
//@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER})
public class ProviderTraceFilter implements Filter {

    private static final String UNIQUE_ID = "tid";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // 打印入参日志
//        DubboServiceRequest serviceRequest = new DubboServiceRequest();
//        serviceRequest.setInterfaceName(invocation.getInvoker().getInterface().getName());
//        serviceRequest.setMethodName(invocation.getMethodName());
//        serviceRequest.setArgs(invocation.getArguments());
//        LOGGER.info("dubbo服务接口入参: " + JSONObject.toJSONString(serviceRequest));
        // 开始时间
        long startTime = System.currentTimeMillis();
        String traceId = RpcContext.getContext().getAttachment(UNIQUE_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = this.getUUID();
        }
        // 设置日志traceId变量
        MDC.put(UNIQUE_ID, traceId);
        RpcContext.getContext().setAttachment(UNIQUE_ID, traceId);
        try {
            Result result = invoker.invoke(invocation);
            // 调用耗时
            long elapsed = System.currentTimeMillis() - startTime;
            // 打印响应日志
//            DubboServiceResponse serviceResponse = new DubboServiceResponse();
//            serviceResponse.setMethodName(invocation.getMethodName());
//            serviceResponse.setInterfaceName(invocation.getInvoker().getInterface().getName());
//            serviceResponse.setArgs(invocation.getArguments());
//            serviceResponse.setResult(new Object[] {result.getValue()});
//            serviceResponse.setSpendTime(elapsed);
//            LOGGER.info("dubbo服务响应成功,返回数据: " + jsonUtils.toJSON(serviceResponse));
            return result;
        } finally {
            MDC.remove(UNIQUE_ID);
        }
    }

    /**
     * 获取UUID
     *
     * @return String UUID
     */
    public String getUUID() {
        String uuid = UUID.randomUUID().toString();
        // 替换-字符
        String uniqueId = UNIQUE_ID + "-" + uuid.replace("-", "");
        return uniqueId;
    }

}
