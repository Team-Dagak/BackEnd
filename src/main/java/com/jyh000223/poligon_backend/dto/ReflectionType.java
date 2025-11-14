package com.jyh000223.poligon_backend.dto;

public enum ReflectionType {

    PROUD("뿌듯해요"),
    RELAXED("여유로웠어요"),
    CHALLENGING("도전적이었어요");

    private final String label;

    ReflectionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    // 프론트(한국어) → Enum 변환
    public static ReflectionType fromLabel(String label) {
        for (ReflectionType type : ReflectionType.values()) {
            if (type.label.equals(label)) return type;
        }
        throw new IllegalArgumentException("Unknown label: " + label);
    }
}
