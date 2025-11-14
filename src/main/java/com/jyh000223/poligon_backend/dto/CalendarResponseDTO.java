package com.jyh000223.poligon_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class CalendarResponseDTO {
    private int year;
    private int month;
    private Map<String, DayResultDTO> days;
}