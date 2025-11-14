package com.jyh000223.poligon_backend.dto;

import java.util.List;

public record DailyGoalDTO(
        Long goalId,
        List<ChecklistDTO> checklists
) {}
