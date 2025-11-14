package com.jyh000223.poligon_backend.enums;

public enum GoalCategory {
    STUDY("#공부 루틴"),
    HEALTH("#건강 루틴"),
    JOB("#취업 준비"),
    FREE("#자율 루틴");

    private final String label;

    GoalCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static GoalCategory fromLabel(String label) {
        for (GoalCategory c : values()) {
            if (c.label.equals(label)) {
                return c;
            }
        }
        throw new IllegalArgumentException("잘못된 카테고리: " + label);
    }
}
