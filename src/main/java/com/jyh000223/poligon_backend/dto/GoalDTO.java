package com.jyh000223.poligon_backend.dto;

import com.jyh000223.poligon_backend.enums.GoalCategory;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalDTO{

    private String goalname;
    private Boolean delayedGoal;
    private LocalDate startdate;
    private LocalDate deadline;
    private Boolean pinned;
    private List<ChecklistDTO> checklists;
    private Boolean hasReflection;
    private GoalCategory goalCategory;
    private Boolean finished;
}

