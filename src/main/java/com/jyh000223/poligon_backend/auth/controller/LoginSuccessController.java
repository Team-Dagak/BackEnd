package com.jyh000223.poligon_backend.auth.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class LoginSuccessController {
    @GetMapping("/login/success")
    public String loginSuccess() {
        return "<h1>✅ 로그인 성공! JWT 쿠키가 발급되었습니다.</h1>";
    }
}