package com.prism.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Prism 平台管理服务启动类
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.prism"})
@MapperScan("com.prism.platform.mapper")
public class PrismPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrismPlatformApplication.class, args);
    }
}
