package com.zjy.dao.common.multiDataSource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yehui
 * 根据线程动态切换数据源
 */
@Configuration
@Slf4j
public class DynamicDataSourceContextHolder {

    /**
     * 设置默认数据源
     */
    public static String DEFAULT_DS = DataSourceKey.MASTER.name();
    /*
     * 当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
     * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
     */
    private static final ThreadLocal<DataSourceKey> contextHolder = ThreadLocal.withInitial(() -> DataSourceKey.MASTER);

    /**
     * 设置数据源
     */
    public static void setDB(DataSourceKey dbType) {
        contextHolder.set(dbType);
    }

    /**
     * 得到数据源
     */
    public static DataSourceKey getDB() {
        return contextHolder.get();
    }

    /**
     * 使用主数据源
     */
    public static void useMasterDataSource() {
        contextHolder.set(DataSourceKey.MASTER);
    }

    /**
     * 移除数据源
     */
    public static void removeDB() {
        contextHolder.remove();
    }

    /**
     * The constant slaveDataSourceKeys.
     */
    public static List<DataSourceKey> slaveDataSourceKeys = new ArrayList<>();

    /**
     * 当使用只读数据源时通过轮循方式选择要使用的数据源
     */
    public static DataSourceKey getRandomDB() {
        int index = (int) (Math.random() * slaveDataSourceKeys.size() * 100);
        int datasourceKeyIndex = index % slaveDataSourceKeys.size();
        return slaveDataSourceKeys.get(datasourceKeyIndex);
    }
}
