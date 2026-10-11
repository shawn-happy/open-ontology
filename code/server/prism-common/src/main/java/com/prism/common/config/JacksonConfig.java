package com.prism.common.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

/**
 * Jackson 全局配置
 * - Long → String（防 JS 精度丢失）
 * - java.time 格式化
 */
@Configuration
public class JacksonConfig {

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                // Long → String
                .serializerByType(Long.class, ToStringSerializer.instance)
                .serializerByType(Long.TYPE, ToStringSerializer.instance)
                // LocalDateTime
                .serializerByType(java.time.LocalDateTime.class,
                        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT)))
                .deserializerByType(java.time.LocalDateTime.class,
                        new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT)))
                // LocalDate
                .serializerByType(java.time.LocalDate.class,
                        new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .deserializerByType(java.time.LocalDate.class,
                        new LocalDateDeserializer(DateTimeFormatter.ofPattern(DATE_FORMAT)));
    }
}
