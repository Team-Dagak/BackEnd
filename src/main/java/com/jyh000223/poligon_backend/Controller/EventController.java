package com.jyh000223.poligon_backend.Controller;

import com.jyh000223.poligon_backend.jwt.JwtTokenProvider;
import com.jyh000223.poligon_backend.repository.GoalRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final GoalRepository goalRepository;


    @GetMapping("/me")
    public ResponseEntity<?> getEventSummary(HttpServletRequest request) {
        String socialId = extractTokenFromCookie(request);

        long unfinished = goalRepository.countBySocialIdAndFinished(socialId, false);
        long needReflection = goalRepository.countBySocialIdAndFinishedTrueAndHasReflectionFalse(socialId);
        long reflected = goalRepository.countBySocialIdAndFinishedTrueAndHasReflectionTrue(socialId);
        long delayed = goalRepository.countBySocialIdAndDelayedGoal(socialId, true);

        return ResponseEntity.ok(
                Map.of(
                        "unfinished", unfinished,
                        "needReflection", needReflection,
                        "reflected", reflected,
                        "delayed", delayed
                )
        );
    }

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
