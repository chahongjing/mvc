package com.zjy.dao.common.sql;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author : fei.wei
 * @Description: mybatis map 映射
 * @date Date : 2020年08月06日 11:19
 */
//@Component
@Intercepts(@Signature(method = "handleResultSets", type = ResultSetHandler.class, args = {Statement.class}))
public class MyBatisMapInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
//        Object target = invocation.getTarget();
//        DefaultResultSetHandler defaultResultSetHandler = (DefaultResultSetHandler) target;
//        //这里通过反射，根据类名称来获取内部类，如果出现变更，则需要修改，不过mybatis的插件目前都是这样一种情况
//        MappedStatement mappedStatement = ReflectUtil.getFieldValue(defaultResultSetHandler, "mappedStatement");
//        String className = StringUtils.substringBeforeLast(mappedStatement.getId(), ".");
//        String methodName = StringUtils.substringAfterLast(mappedStatement.getId(), ".");
//        Method[] methods = Class.forName(className).getDeclaredMethods();
//        if (methods == null) {
//            return invocation.proceed();
//        }
//
//        //找到需要执行的method 注意这里是根据方法名称来查找，如果出现方法重载，需要认真考虑
//        for (Method method : methods) {
//            if (methodName.equalsIgnoreCase(method.getName())) {
//                //如果添加了注解标识，就将结果转换成map
//                MyBatisMapResult map = method.getAnnotation(MyBatisMapResult.class);
//                if (map == null) {
//                    return invocation.proceed();
//                }
//                //进行map的转换
//                Statement statement = (Statement) invocation.getArgs()[0];
//                return result2Map(statement);
//            }
//        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private Object result2Map(Statement statement) throws Throwable {
        ResultSet resultSet = statement.getResultSet();
        if (resultSet == null) {
            return null;
        }
        List<Object> resultList = new ArrayList<Object>();
        Map<Object, Object> map = new HashMap<Object, Object>();
        while (resultSet.next()) {
            map.put(resultSet.getObject(1), resultSet.getObject(2));
        }
        resultList.add(map);
        return resultList;
    }
}
