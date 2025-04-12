package com.example.demo.auth.service;

import com.example.demo.auth.dto.*;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SocialAuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private JwtTokenProvider tokenProvider;
    @Autowired private RestTemplate restTemplate;

    public String login(String provider, String accessToken) {
        SocialUser userInfo = switch (provider) {
            case "kakao" -> fetchFromKakao(accessToken);
            case "google" -> fetchFromGoogle(accessToken);
            default -> throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다.");
        };

        User user = userRepository.findByEmail(userInfo.getEmail());
        if (user == null) {
            user = new User(userInfo.getEmail(), userInfo.getNickname(), userInfo.getProvider());
            userRepository.save(user);
        }

        return tokenProvider.generateToken(
        user.getEmail(),
        user.getProvider()
    );
    }

    private SocialUser fetchFromKakao(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.GET,
            entity,
            KakaoUserResponse.class
        );

        var account = response.getBody().getKakao_account();
        return new SocialUser(account.getEmail(), account.getProfile().getNickname(), "kakao");
    }

    private SocialUser fetchFromGoogle(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GoogleUserResponse> response = restTemplate.exchange(
            "https://www.googleapis.com/oauth2/v2/userinfo",
            HttpMethod.GET,
            entity,
            GoogleUserResponse.class
        );

        GoogleUserResponse body = response.getBody();
        return new SocialUser(body.getEmail(), body.getName(), "google");
    }
}