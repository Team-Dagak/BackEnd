package com.jyh000223.poligon_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "pph_checklist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Checklist {
    @Id
    @GeneratedValue
    private int checklistId;
    @Column(name = "checklist_name")
    private String checklistName;
    @Column(name = "goal_id")
    private Long goalId;  // 필드명을 카멜케이스로!
    @Column(name = "social_id", nullable = false)
    private String socialId;

    @Column(name="clear")
    private boolean clear;

    //calendar에서의 관리용
    @Column(name = "check_date", nullable = false)
    private LocalDate checkDate;
}
