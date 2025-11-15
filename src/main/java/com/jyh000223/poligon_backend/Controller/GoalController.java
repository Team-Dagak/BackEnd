package com.jyh000223.poligon_backend.Controller;

import com.jyh000223.poligon_backend.dto.GoalDTO;
import com.jyh000223.poligon_backend.dto.GoalReflectionRequestDTO;
import com.jyh000223.poligon_backend.dto.ReflectionType;
import com.jyh000223.poligon_backend.entities.Goal;
import com.jyh000223.poligon_backend.entities.GoalReflection;
import com.jyh000223.poligon_backend.enums.GoalCategory;
import com.jyh000223.poligon_backend.jwt.JwtTokenProvider;
import com.jyh000223.poligon_backend.repository.GoalReflectionRepository;
import com.jyh000223.poligon_backend.repository.GoalRepository;
import com.jyh000223.poligon_backend.service.GoalService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
                dto.getHasReflection(),
                dto.getGoalCategory(),
                dto.getFinished()
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
    @GetMapping("/daily/{date}")
    public ResponseEntity<?> getDailyGoals(
            @PathVariable String date,
            HttpServletRequest request
    ) {
        String token = extractTokenFromCookie(request);
        String socialId = jwtTokenProvider.getSocialId(token);

        LocalDate targetDate = LocalDate.parse(date);

        return ResponseEntity.ok(
                goalService.getDailyGoals(socialId, targetDate)
        );
    }
    @PostMapping("/{goalId}/reflection")
    public ResponseEntity<?> createReflection(
            @PathVariable Long goalId,
            @RequestBody GoalReflectionRequestDTO dto,
            HttpServletRequest request
    ) {
        String token = extractTokenFromCookie(request);
        String socialId = jwtTokenProvider.getSocialId(token);

        // 1) 한국어 레이블 → ENUM 변환
        ReflectionType type = ReflectionType.fromLabel(dto.reflectionType());

        // 2) 엔티티 생성 및 저장
        GoalReflection reflection = new GoalReflection(
                null,
                goalId,
                socialId,
                type,
                dto.comment()
        );

        reflectionRepository.save(reflection);

        // ⭐ 3) Goal 업데이트 (finished = true, hasReflection = true)
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        goal.setFinished(true);
        goal.setHasReflection(true);

        goalRepository.save(goal);

        return ResponseEntity.ok("saved");
    }

    @GetMapping("/unfinished")
    public ResponseEntity<?> getUnfinishedGoals(HttpServletRequest request) {
        String socialId = extractTokenFromCookie(request);

        return ResponseEntity.ok(
                goalRepository.findBySocialIdAndFinished(socialId, false)
        );
    }

    @GetMapping("/need-reflection")
    public ResponseEntity<?> getGoalsNeedingReflection(HttpServletRequest request) {
        String socialId = extractTokenFromCookie(request);

        return ResponseEntity.ok(
                goalRepository.findBySocialIdAndFinishedTrueAndHasReflectionFalse(socialId)
        );
    }

    @GetMapping("/reflected")
    public ResponseEntity<?> getReflectedGoals(HttpServletRequest request) {
        String socialId = extractTokenFromCookie(request);

        return ResponseEntity.ok(
                goalRepository.findBySocialIdAndFinishedTrueAndHasReflectionTrue(socialId)
        );
    }



    @GetMapping("/category")
    public ResponseEntity<?> getGoalsByCategory(
            @RequestParam String value,
            HttpServletRequest request
    ) {
        String token = extractTokenFromCookie(request);
        String socialId = jwtTokenProvider.getSocialId(token);

        // 문자열("#공부 루틴") → Enum 변환
        GoalCategory category = GoalCategory.fromLabel(value);

        List<Goal> goals = goalRepository.findBySocialIdAndCategory(socialId, category);
        return ResponseEntity.ok(goals);
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

