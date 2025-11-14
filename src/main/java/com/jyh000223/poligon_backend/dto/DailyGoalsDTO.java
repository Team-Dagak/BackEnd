package com.jyh000223.poligon_backend.dto;

import java.time.LocalDate;
import java.util.Collection;

public record DailyGoalsDTO(
        LocalDate date,
        Collection<DailyGoalDTO> goals
) {}

