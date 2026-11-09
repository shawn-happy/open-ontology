package com.prism.platform.service.impl;

import com.prism.platform.constants.SystemConstants;
import com.prism.platform.vo.CaptchaVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 验证码服务测试（生成/校验/Redis 交互/一次性消费）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("验证码服务")
class CaptchaServiceImplTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private CaptchaServiceImpl captchaService;

    @BeforeEach
    void setUp() {
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Nested
    @DisplayName("生成验证码")
    class GenerateCaptcha {

        @Test
        @DisplayName("生成成功 - 返回 Key 与 Base64 图片并按 5 分钟 TTL 写入 Redis")
        void generateCaptcha_success_returnsKeyAndImageAndStoresCode() {
            CaptchaVO vo = captchaService.generateCaptcha();

            assertThat(vo.getCaptchaKey()).isNotBlank().hasSize(32);
            assertThat(vo.getCaptchaImage()).startsWith("data:image/png;base64,");
            assertThat(vo.getCaptchaImage().length()).isGreaterThan("data:image/png;base64,".length());

            ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
            verify(valueOperations).set(keyCaptor.capture(), codeCaptor.capture(),
                    eq(SystemConstants.CAPTCHA_TTL_SECONDS), eq(TimeUnit.SECONDS));
            assertThat(keyCaptor.getValue())
                    .isEqualTo(SystemConstants.REDIS_CAPTCHA_PREFIX + vo.getCaptchaKey());
            assertThat(codeCaptor.getValue()).hasSize(4);
            assertThat(codeCaptor.getValue()).isEqualTo(codeCaptor.getValue().toLowerCase());
        }

        @Test
        @DisplayName("多次生成 - Key 不重复")
        void generateCaptcha_calledTwice_producesDistinctKeys() {
            CaptchaVO first = captchaService.generateCaptcha();
            CaptchaVO second = captchaService.generateCaptcha();

            assertThat(first.getCaptchaKey()).isNotEqualTo(second.getCaptchaKey());
            verify(valueOperations, org.mockito.Mockito.times(2)).set(anyString(), anyString(),
                    eq(SystemConstants.CAPTCHA_TTL_SECONDS), eq(TimeUnit.SECONDS));
        }
    }

    @Nested
    @DisplayName("校验验证码")
    class VerifyCaptcha {

        @Test
        @DisplayName("Key 为 null - 返回 false 不访问 Redis")
        void verifyCaptcha_nullKey_returnsFalse() {
            assertThat(captchaService.verifyCaptcha(null, "ab12")).isFalse();
            verify(valueOperations, never()).get(anyString());
        }

        @Test
        @DisplayName("验证码为 null - 返回 false 不访问 Redis")
        void verifyCaptcha_nullCode_returnsFalse() {
            assertThat(captchaService.verifyCaptcha("key", null)).isFalse();
            verify(valueOperations, never()).get(anyString());
        }

        @Test
        @DisplayName("Redis 中无验证码（已过期） - 返回 false")
        void verifyCaptcha_codeNotStored_returnsFalse() {
            when(valueOperations.get(SystemConstants.REDIS_CAPTCHA_PREFIX + "k1")).thenReturn(null);

            assertThat(captchaService.verifyCaptcha("k1", "ab12")).isFalse();
            verify(stringRedisTemplate, never()).delete(anyString());
        }

        @Test
        @DisplayName("验证码匹配（忽略大小写） - 返回 true 并删除 Redis 键")
        void verifyCaptcha_matchedIgnoreCase_returnsTrueAndDeletesKey() {
            when(valueOperations.get(SystemConstants.REDIS_CAPTCHA_PREFIX + "k1")).thenReturn("abcd");

            assertThat(captchaService.verifyCaptcha("k1", "ABCD")).isTrue();
            verify(stringRedisTemplate).delete(SystemConstants.REDIS_CAPTCHA_PREFIX + "k1");
        }

        @Test
        @DisplayName("验证码不匹配 - 返回 false 且不删除 Redis 键")
        void verifyCaptcha_mismatch_returnsFalseWithoutDelete() {
            when(valueOperations.get(SystemConstants.REDIS_CAPTCHA_PREFIX + "k1")).thenReturn("abcd");

            assertThat(captchaService.verifyCaptcha("k1", "abce")).isFalse();
            verify(stringRedisTemplate, never()).delete(anyString());
        }
    }
}
