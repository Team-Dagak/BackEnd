package com.jyh000223.poligon_backend.Controller;

import com.jyh000223.poligon_backend.dto.CalendarResponseDTO;
import com.jyh000223.poligon_backend.jwt.JwtTokenProvider;
import com.jyh000223.poligon_backend.service.CalendarService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final JwtTokenProvider jwtTokenProvider;
    private final CalendarService calendarService;

    @GetMapping("/{year}/{month}")
    public CalendarResponseDTO getCalendar(
            @PathVariable int year,
            @PathVariable int month,
            HttpServletRequest request
    ) {
        // ⭐ 1. 쿠키에서 JWT 추출
        String token = extractTokenFromCookie(request);
        if (token == null) {
            throw new RuntimeException("인증 토큰이 없습니다.");
        }

        // ⭐ 2. JWT 유효성 체크

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        // ⭐ 3. JWT에서 socialId 추출
        String socialId = jwtTokenProvider.getSocialId(token);

        // ⭐ 4. 서비스 호출
        return calendarService.getCalendarForUser(socialId, year, month);
    }

    // JWT에서 socialId 추출 메서드
    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        throw new RuntimeException("쿠키에 access_token이 없습니다");
    }
}
