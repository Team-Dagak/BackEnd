package com.jyh000223.poligon_backend.repository;

import com.jyh000223.poligon_backend.entities.GoalReflection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoalReflectionRepository extends JpaRepository<GoalReflection, Long> {

    // 특정 유저가 특정 Goal에 대해 작성한 회고 가져오기
    Optional<GoalReflection> findByGoalIdAndSocialId(Long goalId, String socialId);

    // 회고 존재 여부 확인
    boolean existsByGoalIdAndSocialId(Long goalId, String socialId);

    // 유저가 작성한 모든 회고 조회 (마이페이지/히스토리용)
    List<GoalReflection> findBySocialId(String socialId);
}
