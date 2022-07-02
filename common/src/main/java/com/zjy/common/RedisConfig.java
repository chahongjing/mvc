package com.zjy.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {
//    @Bean
//    public RedisTemplate<String, String> stringRedisTemplate(RedisConnectionFactory connectionFactory) {
////        RedisTemplate<String, Object> template = new RedisTemplate<>();
////        template.setConnectionFactory(redisConnectionFactory);
////        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
////        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);
////        ObjectMapper om = new ObjectMapper();
////        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
////        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
////        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
////        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
////        jacksonSeial.setObjectMapper(om);
////
////        // 值采用json序列化
////        template.setValueSerializer(jacksonSeial);
////        //使用StringRedisSerializer来序列化和反序列化redis的key值
////        template.setKeySerializer(new StringRedisSerializer());
////
////        // 设置hash key 和value序列化模式
////        template.setHashKeySerializer(new StringRedisSerializer());
////        template.setHashValueSerializer(new StringRedisSerializer());
////        template.afterPropertiesSet();
//
//        RedisTemplate<String, String> template = new StringRedisTemplate();
//        template.setConnectionFactory(connectionFactory);
//        return template;
//    }

    @Bean
    public RedisTemplate<String, Object> objRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        return template;
    }

//    @Bean
//    public RedisTemplate<String, Object> objRedisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
////        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
////        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
////        ObjectMapper om = new ObjectMapper();
////        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
////        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
////        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
////        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
////        serializer.setObjectMapper(om);
//
//        FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);
//        //使用StringRedisSerializer来序列化和反序列化redis的key值
//        template.setKeySerializer(new StringRedisSerializer());
//        // 值采用json序列化
//        template.setValueSerializer(serializer);
//
//        // 设置hash key 和value序列化模式
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(serializer);
//        template.afterPropertiesSet();
//        return template;
//    }

    @Bean
    public RedisTemplate<String, byte[]> byteArrayRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）

        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        //om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        // 值采用json序列化
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(RedisSerializer.byteArray());
        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(RedisSerializer.byteArray());
        template.afterPropertiesSet();
        return template;
    }
}
