package com.jyh000223.poligon_backend.dto;

import lombok.*;
import java.time.LocalDate;

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
}

