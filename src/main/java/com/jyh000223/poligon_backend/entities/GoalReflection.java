package com.jyh000223.poligon_backend.entities;

import com.jyh000223.poligon_backend.dto.ReflectionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pph_goal_reflection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalReflection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reflectionId;

    private Long goalId;
    private String socialId;

    @Enumerated(EnumType.STRING)
    private ReflectionType reflectionType;

    @Column(length = 1000)
    private String comment;  // 선택 입력 가능
}

