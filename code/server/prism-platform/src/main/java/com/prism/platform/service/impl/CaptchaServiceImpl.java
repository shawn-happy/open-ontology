package com.prism.platform.service.impl;

import com.prism.platform.constants.SystemConstants;
import com.prism.platform.service.CaptchaService;
import com.prism.platform.vo.CaptchaVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

    private static final String CHAR_POOL = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
    private static final int CODE_LENGTH = 4;
    private static final int IMAGE_WIDTH = 120;
    private static final int IMAGE_HEIGHT = 40;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public CaptchaVO generateCaptcha() {
        String key = UUID.randomUUID().toString().replace("-", "");
        String code = generateCode();

        // 存储到 Redis，TTL 5 分钟
        stringRedisTemplate.opsForValue().set(
                SystemConstants.REDIS_CAPTCHA_PREFIX + key,
                code.toLowerCase(),
                SystemConstants.CAPTCHA_TTL_SECONDS,
                TimeUnit.SECONDS
        );

        // 生成图片
        String imageBase64 = generateImage(code);

        return CaptchaVO.builder()
                .captchaKey(key)
                .captchaImage(imageBase64)
                .build();
    }

    @Override
    public boolean verifyCaptcha(String key, String code) {
        if (key == null || code == null) {
            return false;
        }
        String redisKey = SystemConstants.REDIS_CAPTCHA_PREFIX + key;
        String storedCode = stringRedisTemplate.opsForValue().get(redisKey);
        if (storedCode == null) {
            return false;
        }
        // 校验成功后删除
        boolean matched = storedCode.equalsIgnoreCase(code);
        if (matched) {
            stringRedisTemplate.delete(redisKey);
        }
        return matched;
    }

    private String generateCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
        }
        return sb.toString();
    }

    private String generateImage(String code) {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        Random random = new Random();

        // 背景
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        // 干扰线
        for (int i = 0; i < 6; i++) {
            g.setColor(new Color(random.nextInt(200), random.nextInt(200), random.nextInt(200)));
            int x1 = random.nextInt(IMAGE_WIDTH);
            int y1 = random.nextInt(IMAGE_HEIGHT);
            int x2 = random.nextInt(IMAGE_WIDTH);
            int y2 = random.nextInt(IMAGE_HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

        // 绘制字符
        g.setFont(new Font("Arial", Font.BOLD, 28));
        for (int i = 0; i < code.length(); i++) {
            g.setColor(new Color(random.nextInt(100), random.nextInt(100), random.nextInt(150)));
            g.drawString(String.valueOf(code.charAt(i)), 10 + i * 25, 30);
        }

        g.dispose();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            log.error("生成验证码图片失败", e);
            return "";
        }
    }
}
