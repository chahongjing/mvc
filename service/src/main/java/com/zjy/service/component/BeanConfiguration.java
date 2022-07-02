package com.zjy.service.component;

import com.zjy.baseframework.interfaces.ICache;
import com.zjy.common.RedisConfig;
import com.zjy.service.common.RedisUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    @ConditionalOnBean(RedisConfig.class)
    public ICache redisCache(RedisUtils redisUtils) {
        return new CacheFromRedis(redisUtils);
    }
    @Bean
    @ConditionalOnMissingBean(RedisConfig.class)
    public ICache localCache() {
        return new CacheFromLocal();
    }
}
