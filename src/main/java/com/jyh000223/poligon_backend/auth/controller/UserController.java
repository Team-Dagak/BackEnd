package com.jyh000223.poligon_backend.auth.controller;

import com.jyh000223.poligon_backend.entities.User;
import com.jyh000223.poligon_backend.jwt.JwtTokenProvider;
import com.jyh000223.poligon_backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@CookieValue(value="access_token", required=false) String jwtToken) {
        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // 1. 토큰 검증 및 사용자 정보 추출
        String socialId = jwtTokenProvider.getSocialId(jwtToken);
        String provider = jwtTokenProvider.getProvider(jwtToken);

        // 2. DB에서 유저 정보 조회
        User user = userRepository.findBySocialIdAndProvider(socialId, provider)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));


        // 3. 리턴
        return ResponseEntity.ok(new UserResponseDto(
                user.getEmail(),
                user.getNickname(),
                user.getProvider(),
                user.getSocialId()
        ));
    }

    public record UserResponseDto(String email, String nickname, String provider, String socialId) {}
}
