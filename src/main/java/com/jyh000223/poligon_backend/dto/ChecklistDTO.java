package com.jyh000223.poligon_backend.dto;


import java.time.LocalDate;

public record ChecklistDTO(

        Integer checklistId,     // 수정/삭제 시 필요
        String checklistName,
        Long goalId,
        Boolean clear,
        LocalDate checkDate
) {}