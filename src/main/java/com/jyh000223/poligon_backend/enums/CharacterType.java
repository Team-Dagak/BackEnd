package com.jyh000223.poligon_backend.enums;


public enum CharacterType {
    CIRCLE("circle"),
    SQUARE("square"),
    STAR("star"),
    RAINDROP("raindrop");

    private final String label;

    CharacterType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    // 문자열 → ENUM 매핑
    public static CharacterType fromLabel(String label) {
        for (CharacterType type : values()) {
            if (type.label.equalsIgnoreCase(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid character type: " + label);
    }
}

