package com.zjy.service.component;

import com.zjy.baseframework.common.Constants;
import com.zjy.baseframework.interfaces.ICache;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.shiro.codec.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//@Component
public class CacheFromRedis implements ICache {

    @Autowired
    private RedisUtils redisUtils;
    @Override
    public Object get(String key) {
        String val = redisUtils.get(key);
        return deserialize(val);
    }
    @Override
    public <T> T get(String key, Class<T> clazz) {
        throw new NotImplementedException();
    }

    @Override
    public <T> void set(String key, T value) {
        throw new NotImplementedException();
    }

    @Override
    public boolean delete(String key) {
        redisUtils.del(key);
        return true;
    }

    private static Object deserialize(String str) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decode(str));
             ObjectInputStream ois = new ObjectInputStream(bis);){
            return ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("deserialize session error", e);
        }
    }

    private static String serialize(Object obj) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos);){
            oos.writeObject(obj);
            return Base64.encodeToString(bos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("serialize session error", e);
        }
    }

    @Override
    public Map<String, String> getAll(String key) {
        throw new NotImplementedException();
    }

    public Object hGet(String key, String field) {
        return redisUtils.hGet(key, field);
    }

    public Map<String, String> hGetAll(String key) {
        Map<String, String> result = new HashMap<>();
        Map<Object, Object> kv = redisUtils.hGetAll(key);
        if(MapUtils.isNotEmpty(kv)) {
            for (Map.Entry<Object, Object> entry : kv.entrySet()) {
                result.put(entry.getKey().toString(), Objects.toString(entry.getValue(), Constants.EMPTY_STRING));
            }
        }
        return result;
    }

    public long hSet(String key, String field, String value) {
        redisUtils.hSet(key, field, value);
        return 1;
    }

    public long hDelete(String key, String field) {
        Long res = redisUtils.hDel(key, field);
        return res == null ? 0 : res;
    }

    public long hDelete(String key) {
        return redisUtils.del(key) ? 1 : 0;
    }
}