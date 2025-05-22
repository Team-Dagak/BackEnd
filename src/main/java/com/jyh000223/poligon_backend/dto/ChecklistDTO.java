package com.jyh000223.poligon_backend.dto;

public record ChecklistDTO(
        Integer checklistId,     // 수정/삭제 시 필요
        String checklistName,
        Integer goalId,
        Boolean clear
) {}