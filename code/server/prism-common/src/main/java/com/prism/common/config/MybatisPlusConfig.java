package com.prism.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.prism.common.context.RequestContextHolder;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import jakarta.annotation.PostConstruct;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * MyBatis-Plus 配置（分页、乐观锁、多租户、自动填充）
 * 各服务通过 mybatis-plus.tenant 配置项注入全局表和租户开关
 */
@Configuration
@ConfigurationProperties(prefix = "prism.tenant")
public class MybatisPlusConfig {

    private static final Logger log = LoggerFactory.getLogger(MybatisPlusConfig.class);

    /** 是否启用多租户拦截器（默认 true） */
    private boolean enabled = true;

    /** 租户列名（默认 workspace_code） */
    private String column = "workspace_code";

    /** 全局表列表（不含租户列，租户拦截器跳过） */
    private Set<String> ignoreTables = new HashSet<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Set<String> getIgnoreTables() {
        return ignoreTables;
    }

    public void setIgnoreTables(Set<String> ignoreTables) {
        this.ignoreTables = ignoreTables;
    }

    @PostConstruct
    public void logConfig() {
        log.info("MyBatis-Plus Tenant Config: enabled={}, column={}, ignoreTables={}",
                enabled, column, ignoreTables);
    }

    /**
     * 插件注册（多租户 → 分页 → 乐观锁）
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 多租户拦截器（必须在分页之前）
        if (enabled) {
            interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
                @Override
                public Expression getTenantId() {
                    String workspaceCode = RequestContextHolder.getWorkspaceCode();
                    return new StringValue(workspaceCode != null ? workspaceCode : "");
                }

                @Override
                public String getTenantIdColumn() {
                    return column;
                }

                @Override
                public boolean ignoreTable(String tableName) {
                    return ignoreTables.contains(tableName);
                }
            }));
        }

        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        return interceptor;
    }

    /**
     * 自动填充处理器（createdAt、updatedAt、createdBy、updatedBy）
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, "createdBy", Long.class, RequestContextHolder.getUserId());
                this.strictInsertFill(metaObject, "updatedBy", Long.class, RequestContextHolder.getUserId());
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
                this.strictUpdateFill(metaObject, "updatedBy", Long.class, RequestContextHolder.getUserId());
            }
        };
    }
}
