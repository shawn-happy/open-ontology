package com.prism.platform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务配置（空间活跃时间回写等）
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
}
