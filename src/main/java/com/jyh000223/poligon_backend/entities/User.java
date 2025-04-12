package com.jyh000223.poligon_backend.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "pph_user") // 선택
public class User {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;

    @Column(name="email")
    private String email;

    @Column(name="nickname")
    private String nickname;

    @Column(name="provider")
    private String provider; // "kakao", "google"

    @Column(name="socialId",nullable = false, unique = true)
    private String socialId; // 소셜 플랫폼의 유일 ID (카카오 id, 구글 sub)

    // 👉 필요한 필드만 쓰는 생성자 직접 정의 (id 제외)
    public User(String email, String nickname, String provider, String socialId) {
        this.email = email;
        this.nickname = nickname;
        this.provider = provider;
        this.socialId = socialId;
    }

}
