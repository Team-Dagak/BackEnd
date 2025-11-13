package com.jyh000223.poligon_backend.service;

import com.jyh000223.poligon_backend.dto.ChecklistDTO;
import com.jyh000223.poligon_backend.entities.Checklist;
import com.jyh000223.poligon_backend.entities.Goal;
import com.jyh000223.poligon_backend.repository.ChecklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
                checklist.setGoalId(goal.getGoalId().intValue());
                checklist.setChecklistName(t.checklistName());
                checklist.setSocialId(socialId);
                checklist.setCheckDate(date);  // ⭐ 날짜별 Daily
                checklist.setClear(false);

                checklistRepository.save(checklist);
            }
        }
    }

}
