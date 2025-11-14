package com.jyh000223.poligon_backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DayResultDTO {
    private int cleared;
    private int total;
    private List<Long> clearedGoals = new ArrayList<>();
    private List<Long> pendingGoals = new ArrayList<>();
}
