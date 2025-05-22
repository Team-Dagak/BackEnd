package com.jyh000223.poligon_backend.repository;

import com.jyh000223.poligon_backend.entities.Checklist;
import com.jyh000223.poligon_backend.entities.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChecklistRepository extends JpaRepository<Checklist, Integer> {
    List<Checklist> findBySocialId(String social_id);
    List<Checklist> findByGoalIdAndSocialId(int goal_id, String social_id);
    Optional<Checklist> findByChecklistIdAndSocialId(int checklistId, String  social_id);
}