package com.jyh000223.poligon_backend.repository;

import com.jyh000223.poligon_backend.entities.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.jyh000223.poligon_backend.enums.GoalCategory;

public interface GoalRepository extends JpaRepository<Goal, Long> { 
    List<Goal> findBySocialId(String socialId);

    List<Goal> findBySocialIdAndCategory(String socialId, GoalCategory category);

    List<Goal> findBySocialIdAndFinished(String socialId, boolean finished);

    List<Goal> findBySocialIdAndFinishedTrueAndHasReflectionFalse(String socialId);

    List<Goal> findBySocialIdAndFinishedTrueAndHasReflectionTrue(String socialId);

    List<Goal> findBySocialIdAndDelayedGoal(String socialId, Boolean delayedGoal);

}
