package com.zjy.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class BeanUtils {
    /**
     * 缓存类-Lambda的映射关系
     */
    private static Map<Class, SerializedLambda> CLASS_LAMDBA_CACHE = new ConcurrentHashMap<>();

    /***
     * 转换方法引用为属性名
     * @param fn
     * @return
     */
    public static <T> String convertToFieldName(IGetter<T> fn) {
        SerializedLambda lambda = getSerializedLambda(fn);
        String methodName = lambda.getImplMethodName();
        String prefix = null;
        if(methodName.startsWith("get")){
            prefix = "get";
        }
        else if(methodName.startsWith("is")){
            prefix = "is";
        }
        if(prefix == null){
            log.warn("无效的getter方法: "+methodName);
            return methodName;
        } else {
            String temp = methodName.replaceFirst(prefix, "");
            return lowerFirstCase(temp);
        }
    }

    /***
     * 转换setter方法引用为属性名
     * @param fn
     * @return
     */
    public static <T,R> String convertToFieldName(ISetter<T,R> fn) {
        SerializedLambda lambda = getSerializedLambda(fn);
        String methodName = lambda.getImplMethodName();
        if(!methodName.startsWith("set")){
            log.warn("无效的setter方法: "+methodName);
            return methodName;
        } else {
            String temp = methodName.replaceFirst("set", "");
            return upperFirstCase(temp);
        }
    }

    /***
     * 获取类对应的Lambda
     * @param fn
     * @return
     */
    private static SerializedLambda getSerializedLambda(Serializable fn){
        //先检查缓存中是否已存在
        SerializedLambda lambda = CLASS_LAMDBA_CACHE.get(fn.getClass());
        if(lambda == null){
            try{//提取SerializedLambda并缓存
                Method method = fn.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(Boolean.TRUE);
                lambda = (SerializedLambda) method.invoke(fn);
                CLASS_LAMDBA_CACHE.put(fn.getClass(), lambda);
            }
            catch (Exception e){
                log.error("获取SerializedLambda异常, class="+fn.getClass().getSimpleName(), e);
            }
        }
        return lambda;
    }

    //32为是char类型大小写的差数，-32是小写变大写，+32是大写变小写
    // /**     * 首字母小写     * @param str     * @return     */
    public static String lowerFirstCase(String str){
        char[] chars = str.toCharArray();
        //首字母小写方法，大写会变成小写，如果小写首字母会消失
        chars[0] +=32;
        return String.valueOf(chars);
    }
    /**     * 首字母大写     * @param str     * @return     */
    public static String upperFirstCase(String str){
        char[] chars = str.toCharArray();
        //首字母小写方法，大写会变成小写，如果小写首字母会消失
        chars[0] -=32;
        return String.valueOf(chars);
    }

}