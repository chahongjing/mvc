package com.zjy.rpc_consumer.common.nacos;

import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.common.utils.JacksonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * 从nacos中获取限流规则
 */
public class NacosDataSourceInitFunc implements InitFunc {

    private final Logger logger = LoggerFactory.getLogger(NacosDataSourceInitFunc.class);

    private static final String PARAMS_PREFIX = "${";

    private static final String PREFIX = "sentinel.nacos.datasource";

    private static final String SERVER_ADDR = PREFIX + ".server-addr";

    private static final String GROUP_ID = PREFIX + ".groupId";

    private static final String NAMESPACE = PREFIX + ".namespace";

    private static final String USERNAME = PREFIX + ".username";

    private static final String PASSWORD = PREFIX + ".password";

    private static final String DATA_ID = "dataId";

    private static final String FLOW = PREFIX + ".flow." + DATA_ID;

    private static final String DEGRADE = PREFIX + ".degrade." + DATA_ID;

    private static final String PARAM_FLOW = PREFIX + ".param-flow." + DATA_ID;

    private static final String SYSTEM = PREFIX + ".system." + DATA_ID;


    @Override
    public void init() throws Exception {
        register(FLOW);
        register(DEGRADE);
        register(PARAM_FLOW);
        register(SYSTEM);
    }

    private void register(String dataIdKey) {
        try {
            Properties properties = buildNacosConfigProperties(SERVER_ADDR, NAMESPACE, USERNAME, PASSWORD);
            String dataId = SentinelConfig.getConfig(dataIdKey);
            String groupId = SentinelConfig.getConfig(GROUP_ID);
            if (null == properties || isBlank(dataId) || isBlank(groupId)) {
                logger.warn("【sentinel】degradeRule nacos config not exist!");
                return;
            }
            switch (dataIdKey) {
                case FLOW:
                    ReadableDataSource<String, List<FlowRule>> flowDataSource = new NacosDataSource<>(properties, groupId, dataId,
                            source -> isBlank(source) ? Collections.EMPTY_LIST : JacksonUtils.toObj(source, new TypeReference<List<FlowRule>>() {
                            }));
                    FlowRuleManager.register2Property(flowDataSource.getProperty());
                    break;
                case DEGRADE:
                    ReadableDataSource<String, List<DegradeRule>> degradeDataSource = new NacosDataSource<>(properties, groupId, dataId,
                            source -> isBlank(source) ? Collections.EMPTY_LIST : JacksonUtils.toObj(source, new TypeReference<List<DegradeRule>>() {
                            }));
                    DegradeRuleManager.register2Property(degradeDataSource.getProperty());
                    break;
                case PARAM_FLOW:
                    ReadableDataSource<String, List<ParamFlowRule>> paramFlowDataSource = new NacosDataSource<>(properties, groupId, dataId,
                            source -> isBlank(source) ? Collections.EMPTY_LIST : JacksonUtils.toObj(source, new TypeReference<List<ParamFlowRule>>() {
                            }));
                    ParamFlowRuleManager.register2Property(paramFlowDataSource.getProperty());
                    break;
                case SYSTEM:
                    ReadableDataSource<String, List<SystemRule>> systemDataSource = new NacosDataSource<>(properties, groupId, dataId,
                            source -> isBlank(source) ? Collections.EMPTY_LIST : JacksonUtils.toObj(source, new TypeReference<List<SystemRule>>() {
                            }));
                    SystemRuleManager.register2Property(systemDataSource.getProperty());
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            logger.error("【sentinel】{} nacos config register error!", dataIdKey, e);
        }
        logger.info("【sentinel】{} nacos config register success!", dataIdKey);
    }

    private Properties buildNacosConfigProperties(String serverAddrKey, String namespaceKey, String usernameKey, String passwordKey) {
        String serverAddress = SentinelConfig.getConfig(serverAddrKey);
        String namespace = SentinelConfig.getConfig(namespaceKey);
        String username = SentinelConfig.getConfig(usernameKey);
        String password = SentinelConfig.getConfig(passwordKey);
        if (isBlank(serverAddress) || serverAddress.startsWith(PARAMS_PREFIX)) {
            return null;
        }
        Properties properties = new Properties();
        if (isNotBlank(serverAddress)) {
            properties.put(PropertyKeyConst.SERVER_ADDR, serverAddress);
        }
        if (isNotBlank(namespace)) {
            properties.put(PropertyKeyConst.NAMESPACE, namespace);
        }
        if (isNotBlank(username)) {
            properties.put(PropertyKeyConst.USERNAME, username);
        }
        if (isNotBlank(password)) {
            properties.put(PropertyKeyConst.PASSWORD, password);
        }
        return properties;
    }

    private boolean isBlank(String v) {
        return v == null || "".equals(v.trim());
    }

    private boolean isNotBlank(String v) {
        return !isBlank(v);
    }
}
