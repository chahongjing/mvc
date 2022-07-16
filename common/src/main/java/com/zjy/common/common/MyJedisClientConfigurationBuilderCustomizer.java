package com.zjy.common.common;

import com.zjy.common.utils.ReflectionUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.data.redis.JedisClientConfigurationBuilderCustomizer;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;

//@Component
public class MyJedisClientConfigurationBuilderCustomizer implements JedisClientConfigurationBuilderCustomizer {
    @Override
    public void customize(JedisClientConfiguration.JedisClientConfigurationBuilder clientConfigurationBuilder) {
        GenericObjectPoolConfig config = (GenericObjectPoolConfig) ReflectionUtils.getValue(clientConfigurationBuilder, "poolConfig");
        config.setTestOnBorrow(true);
    }
}
