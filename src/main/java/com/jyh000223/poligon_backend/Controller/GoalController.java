package com.jyh000223.poligon_backend.Controller;

import com.jyh000223.poligon_backend.dto.GoalDTO;
import com.jyh000223.poligon_backend.dto.GoalReflectionRequestDTO;
import com.jyh000223.poligon_backend.dto.ReflectionType;
import com.jyh000223.poligon_backend.entities.Goal;
import com.jyh000223.poligon_backend.entities.GoalReflection;
import com.jyh000223.poligon_backend.jwt.JwtTokenProvider;
import com.jyh000223.poligon_backend.repository.GoalReflectionRepository;
import com.jyh000223.poligon_backend.repository.GoalRepository;
import com.jyh000223.poligon_backend.service.GoalService;
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
    private final GoalService goalService;
    private final GoalReflectionRepository reflectionRepository;
    public GoalController(GoalRepository goalRepository, JwtTokenProvider jwtTokenProvider,
                          GoalService goalService, GoalReflectionRepository reflectionRepository) {
        this.goalRepository = goalRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.goalService = goalService;
        this.reflectionRepository = reflectionRepository;
    }

    @PostMapping
    public ResponseEntity<?> createGoal(@RequestBody GoalDTO dto,
                                        HttpServletRequest request) {

        // 1) socialId
        String token = extractTokenFromCookie(request);
        String socialId = jwtTokenProvider.getSocialId(token);

        // 2) Goal 저장
        Goal goal = new Goal(
                null,
                dto.getGoalname(),
                dto.getDelayedGoal(),
                dto.getStartdate(),
                dto.getDeadline(),
                dto.getPinned(),
                socialId,
                dto.getHasReflection()
        );

        Goal savedGoal = goalRepository.save(goal);

        // ⭐ 3) 프론트에서 받은 checklist 이름을 goalService로 전달
        goalService.createDailyChecklists(savedGoal, dto.getChecklists(), socialId);

        return ResponseEntity.ok(savedGoal);
    }




    // 목표 조회
    @GetMapping
    public ResponseEntity<List<Goal>> getGoals(HttpServletRequest request) {
        String token = extractTokenFromCookie(request);
        String socialId = jwtTokenProvider.getSocialId(token);

        return ResponseEntity.ok(goalRepository.findBySocialId(socialId));
    }

    @PostMapping("/{goalId}/reflection")
    public ResponseEntity<?> createReflection(
            @PathVariable Long goalId,
            @RequestBody GoalReflectionRequestDTO dto,
            HttpServletRequest request
    ) {
        String token = extractTokenFromCookie(request);
        String socialId = jwtTokenProvider.getSocialId(token);

        ReflectionType type = ReflectionType.fromLabel(dto.reflectionType());  // ⭐ 한국어 → ENUM 변환

        GoalReflection reflection = new GoalReflection(
                null,
                goalId,
                socialId,
                type,
                dto.comment()
        );

        reflectionRepository.save(reflection);

        return ResponseEntity.ok("saved");
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

