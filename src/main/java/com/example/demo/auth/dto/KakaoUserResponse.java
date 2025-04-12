package com.example.demo.auth.dto;

public class KakaoUserResponse {
    private KakaoAccount kakao_account;

    public KakaoAccount getKakao_account() { return kakao_account; }

    public static class KakaoAccount {
        private String email;
        private KakaoProfile profile;

        public String getEmail() { return email; }
        public KakaoProfile getProfile() { return profile; }
    }

    public static class KakaoProfile {
        private String nickname;

        public String getNickname() { return nickname; }
    }
}