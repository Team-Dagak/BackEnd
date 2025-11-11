package com.jyh000223.poligon_backend.auth.service;

/*
import com.jyh000223.poligon_backend.auth.dto.*;
import com.jyh000223.poligon_backend.jwt.JwtTokenProvider;
import com.jyh000223.poligon_backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.jyh000223.poligon_backend.user.User;

@Service
public class SocialAuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private JwtTokenProvider tokenProvider;
    @Autowired private RestTemplate restTemplate;

    public String login(String provider, String accessToken) {
        SocialDTO userInfo = switch (provider) {
            case "kakao" -> fetchFromKakao(accessToken);
            case "google" -> fetchFromGoogle(accessToken);
            default -> throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다.");
        };

        System.out.println("🔍 소셜 로그인 응답: " + userInfo);

        User user = userRepository.findBySocialIdAndProvider(userInfo.getSocialId(), userInfo.getProvider())
                .orElseGet(() -> {
                    System.out.println("🆕 신규 유저 등록: " + userInfo.getEmail());
                    User newUser = new User(
                            userInfo.getEmail(),
                            userInfo.getNickname(),
                            userInfo.getProvider(),
                            userInfo.getSocialId()
                    );
                    return userRepository.save(newUser);
                });

        return tokenProvider.generateToken(user.getEmail(), user.getProvider(),user.getSocialId());
    }

    private SocialDTO fetchFromKakao(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                KakaoUserResponse.class
        );

        var body = response.getBody();
        var account = body.getKakao_account();

        // ✅ 카카오 id → socialId로 저장
        return new SocialDTO(
                account.getEmail(),
                account.getProfile().getNickname(),
                "kakao",
                String.valueOf(body.getId()) // id는 Long → String으로
        );
    }

    private SocialDTO fetchFromGoogle(String token) {
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

        // ✅ 구글 id (sub 또는 id) 저장
        return new SocialDTO(
                body.getEmail(),
                body.getName(),
                "google",
                body.getId()
        );
    }
}
*/
