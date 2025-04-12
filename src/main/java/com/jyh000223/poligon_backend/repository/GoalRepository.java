package com.jyh000223.poligon_backend.repository;

import com.jyh000223.poligon_backend.entities.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findBySocialId(String socialId);
}
