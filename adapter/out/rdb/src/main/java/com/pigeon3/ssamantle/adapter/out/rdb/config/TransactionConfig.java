package com.pigeon3.ssamantle.adapter.out.rdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 트랜잭션 관리 설정
 * RDB 어댑터에서 트랜잭션 매니저를 제공
 */
@Configuration
public class TransactionConfig {

    /**
     * DataSource 기반 트랜잭션 매니저 Bean 등록
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
