    package com.jyh000223.poligon_backend.config;

    import com.jyh000223.poligon_backend.entities.User;
    import com.jyh000223.poligon_backend.jwt.JwtTokenProvider;
    import com.jyh000223.poligon_backend.repository.UserRepository;
    import jakarta.servlet.http.Cookie;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.http.ResponseCookie;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.oauth2.core.user.OAuth2User;
    import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
    import org.springframework.stereotype.Component;

    import java.io.IOException;
    import java.util.Map;

    @Component
    public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

        private final UserRepository userRepository;
        private final JwtTokenProvider jwtTokenProvider;

        public OAuth2AuthenticationSuccessHandler(UserRepository userRepository,
                                                  JwtTokenProvider jwtTokenProvider) {
            this.userRepository = userRepository;
            this.jwtTokenProvider = jwtTokenProvider;
        }
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                                            HttpServletResponse response,
                                            Authentication authentication) throws IOException {

            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            final String provider = request.getRequestURI().contains("kakao") ? "kakao" : "google";
            final String email = oAuth2User.getAttribute("email");

            // 👉 nickname 처리
            final String nickname;
            if (provider.equals("kakao")) {
                Object props = oAuth2User.getAttribute("properties");
                if (props instanceof Map<?, ?> map && map.get("nickname") != null) {
                    nickname = map.get("nickname").toString();
                } else {
                    nickname = "카카오유저";
                }
            } else if (provider.equals("google")) {
                nickname = oAuth2User.getAttribute("name");
            } else {
                throw new RuntimeException("지원하지 않는 provider: " + provider);
            }

            // 👉 socialId 처리
            final String socialId;
            if (provider.equals("kakao")) {
                socialId = String.valueOf(oAuth2User.getAttribute("id"));
            } else if (provider.equals("google")) {
                final Object subObj = oAuth2User.getAttribute("sub");
                if (subObj == null) {
                    throw new RuntimeException("❌ 구글 sub 필드가 null입니다.");
                }
               socialId = subObj.toString(); // 👍 안전
            } else {
                throw new RuntimeException("지원하지 않는 provider: " + provider);
            }

            // 👉 유저 DB 저장 or 조회
            User user = userRepository.findBySocialIdAndProvider(socialId, provider)
                    .orElseGet(() -> userRepository.save(
                            new User(email, nickname, provider, socialId)
                    ));

            // 👉 JWT 생성 및 쿠키 저장
            String jwt = jwtTokenProvider.generateToken(user.getEmail(), user.getProvider(), user.getSocialId());

    // 기존 코드 대체
            ResponseCookie cookie = ResponseCookie.from("access_token", jwt)
                    .httpOnly(true)
                    .secure(true) // HTTPS 환경이면 true, 개발 환경에서 false도 가능
                    .sameSite("None") // 크로스도메인 필수
                    .path("/")
                    .maxAge(60 * 60 * 24)
                    .build();

            response.setHeader("Set-Cookie", cookie.toString());
            response.sendRedirect("https://www.team-mole.shop/");

        }

    }


