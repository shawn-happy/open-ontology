package com.prism.platform.controller;

import com.prism.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查接口
 */
@Tag(name = "健康检查", description = "服务健康状态")
@RestController
@RequestMapping("/web/platform")
public class HealthController {

    @Operation(summary = "健康检查", description = "返回服务健康状态")
    @GetMapping("/health")
    public Result<String> health() {
        return Result.ok("Prism Platform is running");
    }
}
