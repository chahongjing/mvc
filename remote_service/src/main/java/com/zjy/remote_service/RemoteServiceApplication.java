package com.zjy.remote_service;

import com.zjy.dao.common.sql.EnableSqlPrint;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableSqlPrint
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = "com.zjy", exclude = {DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class})
public class RemoteServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RemoteServiceApplication.class, args);
    }

}
