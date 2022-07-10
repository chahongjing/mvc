package com.zjy.service.configuration;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.zjy.common.utils.FastJsonUtils;
import com.zjy.common.utils.JacksonUtil;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return JacksonUtil::processBuilder;
    }

    @Bean
    public FastJsonConfig fastJsonConfig() {
        // 创建配置类
        return FastJsonUtils.getFastJsonConfig();
    }
}