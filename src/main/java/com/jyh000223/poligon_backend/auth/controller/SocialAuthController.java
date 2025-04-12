package com.jyh000223.poligon_backend.auth.controller;
/*
import com.jyh000223.poligon_backend.auth.service.SocialAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class SocialAuthController {

    @Autowired private SocialAuthService socialAuthService;

    @PostMapping("/{provider}")
    public ResponseEntity<TokenResponse> login(
        @PathVariable String provider,
        @RequestHeader("Authorization") String tokenHeader
    ) {
        String accessToken = tokenHeader.replace("Bearer", "").trim();
        String jwt = socialAuthService.login(provider, accessToken);
        return ResponseEntity.ok(new TokenResponse(jwt));
    }

    public record TokenResponse(String token) {}


}
*/