package com.example.demo.auth.dto;

public class SocialUser {
    private String email;
    private String nickname;
    private String provider;

    public SocialUser(String email, String nickname, String provider) {
        this.email = email;
        this.nickname = nickname;
        this.provider = provider;
    }

    public String getEmail() { return email; }
    public String getNickname() { return nickname; }
    public String getProvider() { return provider; }
}