package com.jyh000223.poligon_backend.Controller;

import com.jyh000223.poligon_backend.dto.GoalDTO;
import com.jyh000223.poligon_backend.entities.Goal;
import com.jyh000223.poligon_backend.jwt.JwtTokenProvider;
import com.jyh000223.poligon_backend.repository.GoalRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalRepository goalRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public GoalController(GoalRepository goalRepository, JwtTokenProvider jwtTokenProvider) {
        this.goalRepository = goalRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 목표 등록
    @PostMapping
    public ResponseEntity<?> createGoal(@RequestBody GoalDTO dto,
                                        HttpServletRequest request) {
        String token = extractTokenFromCookie(request);
        String socialId = jwtTokenProvider.getSocialId(token);
        Boolean delayed = dto.getDelayedGoal();

        if (delayed == null) delayed = false; // 기본값 보완

        Goal goal = new Goal(null,
                dto.getGoalname(),
                delayed,
                dto.getStartdate(),
                dto.getDeadline(),
                dto.getPinned(),
                socialId);

        return ResponseEntity.ok(goalRepository.save(goal));
    }

    // 목표 조회
    @GetMapping
    public ResponseEntity<List<Goal>> getGoals(HttpServletRequest request) {
        String token = extractTokenFromCookie(request);
        String socialId = jwtTokenProvider.getSocialId(token);

        return ResponseEntity.ok(goalRepository.findBySocialId(socialId));
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

