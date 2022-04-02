package com.zjy.dao.common.multiDataSource;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zjy.dao.common.sql.SqlPrint;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yehui
 * 数据源配置类
 */
@Configuration
@Slf4j
// 扫描dao
@MapperScan(basePackages = {"com.zjy.dao"}, sqlSessionFactoryRef = "sqlSessionFactoryBean")
public class DataSourceConfig {

    @Resource
    private SqlPrint sqlPrint;

    /**
     * 主数据
     *
     * @return data source
     */
    @Bean("master")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource master() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 从数据库
     *
     * @return data source
     */
    @Bean("slave")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slave() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 从数据库
     *
     * @return data source
     */
    @Bean("migration")
    @ConfigurationProperties(prefix = "spring.datasource.migration")
    public DataSource migration() {
        return DataSourceBuilder.create().build();
    }


    /**
     * 配置动态数据源
     *
     * @return
     */
    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>(4);
        dataSourceMap.put(DataSourceKey.master.name(), master());
        dataSourceMap.put(DataSourceKey.slave.name(), slave());

        //设置默认的数据源
        dynamicRoutingDataSource.setDefaultTargetDataSource(master());
        // 多个slave数据源在此添加，自定义key，用于轮询
//        dataSourceMap.put(DataSourceKey.slave.name() + "1", slave());
        //设置目标数据源
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);
//        //将数据源的key放在集合中判断是否正常
        DynamicDataSourceContextHolder.slaveDataSourceKeys.addAll(dataSourceMap.keySet());
//
//        //实现负载均衡算法   将 Slave 数据源的 key 放在集合中，用于轮循
//        DynamicDataSourceContextHolder.slaveDataSourceKeys.addAll(dataSourceMap.keySet());
//        DynamicDataSourceContextHolder.slaveDataSourceKeys.remove(DataSourceKey.migration.name());
        return dynamicRoutingDataSource;
    }

    /**
     * 设置工厂类
     */
    @Bean("sqlSessionFactoryBean")
    public FactoryBean<SqlSessionFactory> sqlSessionFactoryBean() {
//        使用mybatisplus要使用MybatisSqlSessionFactoryBean。SqlSessionFactoryBean
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource());

        //此处设置为了解决找不到mapper文件的问题
        try {
            sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().
                    getResources("classpath:mapper/*Mapper.xml"));
//            sqlSessionFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml"));
            MybatisConfiguration configuration = new MybatisConfiguration();
//            configuration.setLogImpl(StdOutImpl.class);//标准输出日志
            configuration.setLogImpl(NoLoggingImpl.class);// 不输出日志（）
            configuration.setMapUnderscoreToCamelCase(true);// 开启驼峰命名
            configuration.setLazyLoadingEnabled(false);
            configuration.setCallSettersOnNulls(true);// 开启在属性为null也调用setter方法
            sqlSessionFactoryBean.setConfiguration(configuration);
            sqlSessionFactoryBean.setTypeEnumsPackage("com.zjy.entity.enums");
            if(sqlPrint != null) {
                sqlSessionFactoryBean.setPlugins(sqlPrint);
            }

//            sqlSessionFactoryBean.setTypeAliasesPackage(DataSourceConfiguration.TYPE_ALIASE);
//            sqlSessionFactoryBean.setTypeHandlersPackage(DataSourceConfiguration.TYPE_HANDLE_PACKAGE);
//            sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
//            TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactoryBean.getObject().getConfiguration().getTypeHandlerRegistry();
//            typeHandlerRegistry.register(DownTaskStatus.class, CodeEnumTypeHandler.class);
            sqlSessionFactoryBean.setTypeHandlersPackage(CodeEnumTypeHandler.class.getPackage().getName());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlSessionFactoryBean;
    }

    /**
     * 事物管理器
     */
    @Bean("transactionManager")
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }
}