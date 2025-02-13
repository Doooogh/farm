package com.doooogh.farm.auth.controller;

import com.doooogh.farm.auth.service.CaptchaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CaptchaController {

    private final CaptchaService captchaService;

    /**
     * 生成图形验证码并返回给前端。
     *
     * @param username 用户名
     * @return 图形验证码
     */
    @GetMapping("/captcha")
    public ResponseEntity<byte[]> getCaptcha(@RequestParam String username) {
        BufferedImage captchaImage = captchaService.generateCaptchaImage(username);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(captchaImage, "png", baos);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
        byte[] imageBytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return ResponseEntity.ok().headers(headers).body(imageBytes);
    }
} 