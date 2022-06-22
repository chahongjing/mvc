package com.zjy.dao.common.multiDataSource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author yehui
 * 多数据源的选择
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        log.debug("切换到【{}】数据源", DynamicDataSourceContextHolder.getDB());
        return DynamicDataSourceContextHolder.getDB();
    }
}