package com.jyh000223.poligon_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://localhost:5173", "https://jyhdevstore.store")
                .allowCredentials(true) // 인증(쿠키 등) 허용
                .allowedMethods("*")
                .allowedHeaders("*")
                .maxAge(3600);         // preflight 요청 캐시 시간(초)
    }
}
