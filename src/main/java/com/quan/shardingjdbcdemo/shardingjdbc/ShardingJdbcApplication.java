package com.quan.shardingjdbcdemo.shardingjdbc;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.quan.shardingjdbcdemo.shardingjdbc.service.CommonService;
import com.quan.shardingjdbcdemo.shardingjdbc.service.SpringPojoService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;


// 由于shardingjdbc 默认使用HikariDataSource,在yml文件中指定了type是DruidDataSource,
// druid-spring-boot-starter会根据DruidDataSourceAutoConfigure自动创建DruidDataSource
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@MapperScan(basePackages = "com.quan.shardingjdbcdemo.shardingjdbc.mapper")
public class ShardingJdbcApplication {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext applicationContext = SpringApplication.run(ShardingJdbcApplication.class, args)) {
            process(applicationContext);
        }
    }

    private static void process(final ConfigurableApplicationContext applicationContext) {
        CommonService commonService = getCommonService(applicationContext);
        commonService.initEnvironment();
        commonService.processSuccess(false);
        try {
            commonService.processFailure();
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
            commonService.printData(false);
        } finally {
            commonService.cleanEnvironment();
        }
    }

    private static CommonService getCommonService(final ConfigurableApplicationContext applicationContext) {
        return applicationContext.getBean(SpringPojoService.class);
    }

}
