package com.zjy.service.component;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjy.baseframework.interfaces.ICache;
import com.zjy.common.common.RedisConfig;
import com.zjy.service.common.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class BeanConfiguration {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FastJsonConfig fastJsonConfig;

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

    @Bean
    public HttpMessageConverters fastJsonMsgConverters() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
//        supportedMediaTypes.add(MediaType.TEXT_HTML);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        converter.setFastJsonConfig(fastJsonConfig);
        //支持text 转string
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        return new HttpMessageConverters(converter, stringHttpMessageConverter);
    }

    //    @Bean
    public HttpMessageConverters jacksonMsgConvert() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
//        supportedMediaTypes.add(MediaType.TEXT_HTML);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        converter.setObjectMapper(objectMapper);
        //支持text 转string
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        return new HttpMessageConverters(converter, stringHttpMessageConverter);
    }
}
