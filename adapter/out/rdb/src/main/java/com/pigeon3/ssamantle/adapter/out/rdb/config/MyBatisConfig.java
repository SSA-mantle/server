package com.pigeon3.ssamantle.adapter.out.rdb.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis 설정
 */
@Configuration
@MapperScan("com.pigeon3.ssamantle.adapter.out.rdb.**.mapper")
public class MyBatisConfig {
    // Mapper 인터페이스 스캔 설정
}
