package com.example.demo.user;

public class User {
    private String email;
    private String nickname;
    private String provider;

    public User(String email, String nickname, String provider) {
        this.email = email;
        this.nickname = nickname;
        this.provider = provider;
    }

    public String getEmail() { return email; }
    public String getNickname() { return nickname; }
    public String getProvider() { return provider; }
}
