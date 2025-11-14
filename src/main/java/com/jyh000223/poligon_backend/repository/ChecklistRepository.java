package com.jyh000223.poligon_backend.repository;

import com.jyh000223.poligon_backend.entities.Checklist;
import com.jyh000223.poligon_backend.entities.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChecklistRepository extends JpaRepository<Checklist, Integer> {
    List<Checklist> findBySocialId(String social_id);

    List<Checklist> findByGoalIdAndSocialId(Long goal_id, String social_id);

    Optional<Checklist> findByChecklistIdAndSocialId(int checklistId, String  social_id);

    List<Checklist> findBySocialIdAndCheckDateBetween(String socialId, LocalDate start, LocalDate end);

    List<Checklist> findBySocialIdandCheckDate(String socialId, LocalDate date);
}