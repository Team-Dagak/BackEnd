package com.jyh000223.poligon_backend.repository;

import com.jyh000223.poligon_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findBySocialId(String socialId);

    Optional<User> findBySocialIdAndProvider(String socialId, String provider);

    // Optional<User> findBySocialIdAndProvider(String socialId, String provider); // 더 정밀하게도 가능
}
