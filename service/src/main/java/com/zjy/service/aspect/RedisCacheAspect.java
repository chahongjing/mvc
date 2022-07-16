package com.zjy.service.aspect;

import com.zjy.baseframework.annotations.RedisCache;
import com.zjy.baseframework.common.RedisKeyUtils;
import com.zjy.entity.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Aspect
@Order(110)
@Component
public class RedisCacheAspect {
    @Lazy
    @Resource
    private RedisTemplate<String, Object> objRedisTemplate;
    Pattern pattern = Pattern.compile("#\\{([^}]+)}");
    SpelExpressionParser parser = new SpelExpressionParser();
    LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Pointcut("@annotation(com.zjy.baseframework.annotations.RedisCache)")
    public void cache() {
    }

    @Around(value = "cache() && @annotation(redisCache)")
    public Object around(ProceedingJoinPoint pjp, RedisCache redisCache) throws Throwable {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        if (redisCache == null) {
            return pjp.proceed();
        }
        Parameter[] parameters = targetMethod.getParameters();
        String key = getKey(redisCache, parameters, pjp, targetMethod);
        Object result = objRedisTemplate.opsForValue().get(key);
        // 没有值
        if (result == null) {
            Boolean exists = objRedisTemplate.hasKey(key);
            // 缓存的null，直接返回
            if (exists != null && exists) {
                return result;
            } else {
                // 没有key
                result = pjp.proceed();
                objRedisTemplate.opsForValue().set(key, result, redisCache.expire(), redisCache.timeUnit());
            }
        } else {
//            // 如果是fastjson需要将jsonobject转为对应的类型，如是是jackson，则不需要转换
//            Class<?> returnType = targetMethod.getReturnType();
//            Class<?> trueType = getTrueType(targetMethod);
//            if(Collection.class.isAssignableFrom(returnType)) {
//                result = jsonUtils.parseList(jsonUtils.toJSON(result), trueType);
//            } else if(returnType.isArray()) {
//                List<?> temp = jsonUtils.parseList(jsonUtils.toJSON(result), trueType);
//                Object[] r = (Object[])Array.newInstance(trueType, temp.size());
//                for (int i = 0; i < temp.size(); i++) {
//                    r[i] = temp.get(i);
//                }
//                return r;
//            } else if(result instanceof JSONObject) {
//                result = jsonUtils.parse((JSONObject)result, trueType);
//            }
        }
        return result;
    }

    public Class getTrueType(Method method) {
        Type genericReturnType = method.getGenericReturnType();
        Class realType;
        if (Collection.class.isAssignableFrom(method.getReturnType())) {
            realType = (Class) ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
        } else if (method.getReturnType().isArray()) {
            realType = method.getReturnType().getComponentType();
        } else {
            realType = method.getReturnType();
        }
        return realType;
    }

    private String getKey(RedisCache redisCache, Parameter[] parameters, ProceedingJoinPoint pjp, Method method) {
        String key = RedisKeyUtils.KEY_PREFIX + pjp.toShortString() + ":" + redisCache.key();
        EvaluationContext spelContext = null;
        Matcher matcher = pattern.matcher(key);
        String newKey = key;
        while (matcher.find()) {
            if (spelContext == null) {
                spelContext = bindParam(method, pjp.getArgs());
            }
            Object value = parser.parseExpression("#" + matcher.group(1)).getValue(spelContext);
            newKey = newKey.replace(matcher.group(0), Objects.toString(value));
        }
        return newKey;
    }

    private EvaluationContext bindParam(Method method, Object[] args) {
        //将参数名与参数值对应起来
        EvaluationContext context = new StandardEvaluationContext();
        //获取方法的参数名
        String[] params = discoverer.getParameterNames(method);
        if (params == null) return context;
        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], args[len]);
        }
        return context;
    }

    public static void main(String[] args) {
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("#root.name");
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setName("张三");
        System.out.println(expression.getValue(userInfo));
//        EvaluationContext context = this.bindParam(method, parameters);
//        expression.getValue(context);
    }
}


