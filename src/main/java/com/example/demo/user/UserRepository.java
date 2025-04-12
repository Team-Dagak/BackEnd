package com.example.demo.user;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    public User findByEmail(String email) {
        // 임시: 항상 null 반환해서 새로운 유저 생성
        return null;
    }

    public void save(User user) {
        // 임시 저장 로직 (아무 동작 안 함)
        System.out.println("User saved: " + user.getEmail());
    }
}
