package com.jyh000223.poligon_backend.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

import lombok.*;

@Entity
@Table(name = "pph_goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long goalId;


    @Column(name = "goalname")
    private String goalname;

    @Column(name = "delayedGoal", nullable = false)
    private Boolean delayedGoal;

    @Column(name = "startdate")
    private LocalDate startdate;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "social_id", nullable = false)
    private String socialId;
}
