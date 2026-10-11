package com.prism.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 异步任务配置（使用虚拟线程）
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * 异步任务执行器（使用虚拟线程）
     */
    @Bean("asyncExecutor")
    public Executor asyncExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    /**
     * Spring 默认异步执行器（兼容 @Async 注解）
     */
    @Bean("taskExecutor")
    public Executor taskExecutor() {
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(256);
        executor.setThreadNamePrefix("prism-async-");
        executor.initialize();
        return executor;
    }
}
