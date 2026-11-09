package com.prism.platform.controller;

import com.prism.common.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 健康检查控制器 MockMvc 薄壳测试（路由 / 统一响应结构）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("健康检查控制器")
class HealthControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new HealthController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /web/health：返回成功码与运行提示语")
    void health_always_returnsOkMessage() throws Exception {
        mockMvc.perform(get("/web/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.msg").value("操作成功"))
                .andExpect(jsonPath("$.data").value("Prism Platform is running"));
    }
}
