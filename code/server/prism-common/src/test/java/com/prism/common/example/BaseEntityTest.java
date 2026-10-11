package com.prism.common.example;

import com.prism.common.entity.BaseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * BaseEntity 单元测试示例
 * 演示 JUnit 5 + AssertJ 的基本用法
 */
class BaseEntityTest {

    // 创建具体子类用于测试（BaseEntity 是抽象类）
    private static class TestEntity extends BaseEntity {
    }

    private TestEntity entity;

    @BeforeEach
    void setUp() {
        entity = new TestEntity();
    }

    @Test
    @DisplayName("设置 ID 后应能正确获取")
    void shouldSetAndGetId() {
        // Given
        Long expectedId = 123L;

        // When
        entity.setId(expectedId);

        // Then
        assertThat(entity.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("设置时间字段后应能正确获取")
    void shouldSetAndGetTimestamps() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        // Then
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("新创建的实体 deleted 应为 null（未设置）")
    void shouldHaveNullDeletedWhenNew() {
        assertThat(entity.getDeleted()).isNull();
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 100L, 999999L})
    @DisplayName("支持多种 ID 值")
    void shouldAcceptVariousIdValues(Long id) {
        entity.setId(id);
        assertThat(entity.getId()).isEqualTo(id);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 张三",
            "2, 李四",
            "3, 王五"
    })
    @DisplayName("CSV 参数化测试示例")
    void shouldHandleCsvParameters(Long id, String name) {
        entity.setId(id);
        // 这里只是演示，BaseEntity 没有 name 字段
        assertThat(entity.getId()).isPositive();
    }
}
