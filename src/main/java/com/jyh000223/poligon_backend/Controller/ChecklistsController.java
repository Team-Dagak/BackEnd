package com.jyh000223.poligon_backend.Controller;

import com.jyh000223.poligon_backend.dto.ChecklistDTO;
import com.jyh000223.poligon_backend.entities.Checklist;
import com.jyh000223.poligon_backend.jwt.JwtTokenProvider;
import com.jyh000223.poligon_backend.repository.ChecklistRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/Checklists")
public class ChecklistsController {
    private final ChecklistRepository checklistRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public ChecklistsController(ChecklistRepository checklistRepository,
                                JwtTokenProvider jwtTokenProvider) {
        this.checklistRepository = checklistRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 유저의 특정 goal 하위 checklist 전체 조회
    @GetMapping("/goal/{goalId}")
    public ResponseEntity<List<Checklist>> getChecklistsByGoal(
            @PathVariable int goalId,
            HttpServletRequest request) {
        String socialId = String.valueOf(getSocialIdFromCookie(request));
        List<Checklist> checklists = checklistRepository.findByGoalIdAndSocialId(goalId, socialId);
        return ResponseEntity.ok(checklists);
    }

    // 유저의 전체 checklist 조회(필요하면)
    @GetMapping
    public ResponseEntity<List<Checklist>> getAllChecklists(HttpServletRequest request) {
        String socialId = String.valueOf(getSocialIdFromCookie(request));
        return ResponseEntity.ok(checklistRepository.findBySocialId(socialId));
    }

    // checklist 등록
    @PostMapping
    public ResponseEntity<Checklist> addChecklist(
            @RequestBody ChecklistDTO dto,
            HttpServletRequest request) {

        String socialId = getSocialIdFromCookie(request);

        Checklist checkList = new Checklist();
        checkList.setChecklistName(dto.checklistName());
        checkList.setGoalId(dto.goalId()); // ⭐ null이면 일반 To-Do
        checkList.setClear(dto.clear() != null ? dto.clear() : false);
        checkList.setSocialId(socialId);
        checkList.setCheckDate(dto.checkDate() != null ? dto.checkDate() : LocalDate.now()); // ⭐ 날짜 지정

        Checklist saved = checklistRepository.save(checkList);
        return ResponseEntity.ok(saved);
    }
    // checklist 수정
    @PutMapping("/{checklistId}")
    public ResponseEntity<Checklist> updateChecklist(
            @PathVariable int checklistId,
            @RequestBody ChecklistDTO dto,
            HttpServletRequest request) {
        String socialId = String.valueOf(getSocialIdFromCookie(request));

        Checklist checklist = checklistRepository.findByChecklistIdAndSocialId(checklistId, socialId)
                .orElseThrow(() -> new RuntimeException("체크리스트를 찾을 수 없습니다"));

        if (dto.checklistName() != null)
            checklist.setChecklistName(dto.checklistName());
        if (dto.clear() != null)
            checklist.setClear(dto.clear());
        // goal_id는 수정 제한 가능(필요시 추가)

        Checklist updated = checklistRepository.save(checklist);
        return ResponseEntity.ok(updated);
    }

    // checklist 삭제
    @DeleteMapping("/{checklistId}")
    public ResponseEntity<?> deleteChecklist(
            @PathVariable int checklistId,
            HttpServletRequest request) {
        String socialId = String.valueOf(getSocialIdFromCookie(request));

        Checklist checklist = checklistRepository.findByChecklistIdAndSocialId(checklistId, socialId)
                .orElseThrow(() -> new RuntimeException("체크리스트를 찾을 수 없습니다"));

        checklistRepository.delete(checklist);
        return ResponseEntity.ok().build();
    }



    // JWT에서 socialId 추출 메서드
    private String getSocialIdFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    return jwtTokenProvider.getSocialId(token);
                }
            }
        }
        throw new RuntimeException("쿠키에 access_token이 없습니다");
    }
}

