package com.zjy.dao.common.sql;

import com.google.common.base.Stopwatch;
import com.zjy.baseframework.interfaces.IBaseEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Intercepts({
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
})
public class SqlPrint implements Interceptor {

    // 没有加注解，根据此判断是否打印sql
    private boolean defaultPrint = true;
    /**
     * 缓存
     * key : statement id,
     * value : 是否打印
     */
    private Map<String, Boolean> mappedStatementPrintSqlMap = new ConcurrentHashMap<>();

    public SqlPrint() {
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Stopwatch stopwatch = Stopwatch.createStarted();
        Object result = invocation.proceed();
        long executionTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        try {
            logSql(invocation, executionTime);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("打印sql异常", e);
            }
            // 新增功能不影响原有功能
        }
        return result;
    }

    /**
     * 打印sql
     */
    private void logSql(Invocation invocation, long executionTime) throws ClassNotFoundException, NoSuchMethodException {
        StatementHandler stmtHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaStmtHandler = SystemMetaObject.forObject(stmtHandler);
        MappedStatement mappedStatement = (MappedStatement) metaStmtHandler.getValue("delegate.mappedStatement");
        // 获取方法上注解
        String fullMapperMethod = mappedStatement.getId();

        // 没有注解, 默认不打sql, 直接返回
        // 有注解, 指定不打印, 直接返回
        if (mappedStatementPrintSqlMap.get(fullMapperMethod) == null) {
            boolean logSql = needLogSql(fullMapperMethod);
            mappedStatementPrintSqlMap.put(fullMapperMethod, logSql);
            mappedStatementPrintSqlMap.put(fullMapperMethod + "_COUNT", logSql);
        }

        // 方法是否要打印sql
        if (!mappedStatementPrintSqlMap.get(fullMapperMethod)) {
            log.info("执行时间【{} ms】方法:{}", executionTime, getMapperMethodName(fullMapperMethod));
            return;
        }

        // 获取参数类型
        BoundSql boundSql;
        boundSql = (BoundSql) metaStmtHandler.getValue("delegate.boundSql");
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // 解析?对应的参数
        List<String> parameters = parseParameter(metaStmtHandler, mappedStatement, boundSql, parameterMappings);
        String sql = boundSql.getSql();
        for (String value : parameters) {
            sql = sql.replaceFirst("\\?", value);
        }
        log.info("执行时间【{} ms】方法:{}. SQL: {}", executionTime, getMapperMethodName(fullMapperMethod), beautifySql(sql));
    }

    /**
     * 解析参数
     */
    private List<String> parseParameter(MetaObject metaStmtHandler, MappedStatement mappedStatement, BoundSql boundSql, List<ParameterMapping> parameterMappings) {
        List<String> parameters = new ArrayList<>();
        if (parameterMappings != null) {
            Object parameterObject = metaStmtHandler.getValue("delegate.boundSql.parameterObject");
            Configuration configuration = mappedStatement.getConfiguration();
            MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
            for (ParameterMapping parameterMapping : parameterMappings) {
                TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    //  参数值
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    //  获取参数名称
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        // 获取参数值
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        // 如果是单个值则直接赋值
                        value = parameterObject;
                    } else {
                        value = metaObject == null ? null : metaObject.getValue(propertyName);
                    }

                    if (value == null) {
                        parameters.add("null");
                    } else if (value instanceof Number) {
                        parameters.add(String.valueOf(value));
                    } else if (value instanceof IBaseEnum) {
                        parameters.add(String.valueOf(((IBaseEnum) value).getValue()));
                    } else {
                        StringBuilder builder = new StringBuilder();
                        builder.append("'");
                        if (value instanceof Date) {
                            builder.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) value));
                        } else if (value instanceof String) {
                            builder.append(value);
                        }
                        builder.append("'");
                        parameters.add(builder.toString());
                    }
                }
            }
        }
        return parameters;
    }

    /**
     * 获取方法或者类上的sqlLog注解, 判断是否打sql
     */
    private boolean needLogSql(String fullMapperMethod) throws ClassNotFoundException, NoSuchMethodException {
        Class<?> mapperClass = getMapperClass(fullMapperMethod);
        // 目标方法
        String targetMethodName = getMapperMethodName(fullMapperMethod);
        // 拿不到参数类型列表?, 这里循环下, 正好mybatis不能重载
        return !Arrays.stream(mapperClass.getMethods())
                .filter(method -> method.getName().equals(targetMethodName))
                .findFirst()
                .map(method -> {
                    // 获取方法上注解
                    SqlLog sqlLog = method.getAnnotation(SqlLog.class);
                    if (sqlLog == null) {
                        sqlLog = mapperClass.getAnnotation(SqlLog.class);
                    }
                    return (sqlLog == null && !defaultPrint) || (sqlLog != null && !sqlLog.print());
                }).orElse(false);
    }

    private String getMapperMethodName(String fullMapperMethod) {
        return fullMapperMethod.substring(fullMapperMethod.lastIndexOf(".", fullMapperMethod.lastIndexOf(".") - 1) + 1);
//        return fullMapperMethod.substring(fullMapperMethod.lastIndexOf(".") + 1);
    }

    private Class<?> getMapperClass(String fullMapperMethod) throws ClassNotFoundException {
        String mapperClassName = fullMapperMethod.substring(0, fullMapperMethod.lastIndexOf("."));
        return Class.forName(mapperClassName);
    }

    private String beautifySql(String sql) {
        return sql.replaceAll("[\\s\n ]+", " ");
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    public boolean isDefaultPrint() {
        return defaultPrint;
    }

    public void setDefaultPrint(boolean defaultPrint) {
        log.debug("defaultPrintSql = {}", defaultPrint);
        this.defaultPrint = defaultPrint;
    }
}