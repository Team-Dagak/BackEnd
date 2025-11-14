package com.jyh000223.poligon_backend.service;

import com.jyh000223.poligon_backend.dto.ChecklistDTO;
import com.jyh000223.poligon_backend.dto.DailyGoalDTO;
import com.jyh000223.poligon_backend.dto.DailyGoalsDTO;
import com.jyh000223.poligon_backend.entities.Checklist;
import com.jyh000223.poligon_backend.entities.Goal;
import com.jyh000223.poligon_backend.repository.ChecklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final ChecklistRepository checklistRepository;

    public void createDailyChecklists(Goal goal,
                                      List<ChecklistDTO> templates,
                                      String socialId) {

        LocalDate start = goal.getStartdate();
        LocalDate end = goal.getDeadline();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            for (ChecklistDTO t : templates) {

                Checklist checklist = new Checklist();
                checklist.setGoalId(goal.getGoalId());
                checklist.setChecklistName(t.checklistName());
                checklist.setSocialId(socialId);
                checklist.setCheckDate(date);  // ⭐ 날짜별 Daily
                checklist.setClear(false);

                checklistRepository.save(checklist);
            }
        }
    }

    public DailyGoalsDTO getDailyGoals(String socialId, LocalDate date) {

        // 1) 해당 날짜에 생성된 checklist들 조회
        List<Checklist> dailyList =
                checklistRepository.findBySocialIdandCheckDate(socialId, date);

        // 2) goalId 기준으로 그룹핑
        Map<Long, DailyGoalDTO> map = new HashMap<>();

        for (Checklist c : dailyList) {

            map.putIfAbsent(
                    c.getGoalId(),
                    new DailyGoalDTO(
                            c.getGoalId(),
                            new ArrayList<>()
                    )
            );

            map.get(c.getGoalId()).checklists().add(
                    new ChecklistDTO(
                            c.getChecklistId(),
                            c.getChecklistName(),
                            c.getGoalId(),
                            c.isClear(),
                            c.getCheckDate()
                    )
            );
        }

        return new DailyGoalsDTO(date, map.values());
    }

}
