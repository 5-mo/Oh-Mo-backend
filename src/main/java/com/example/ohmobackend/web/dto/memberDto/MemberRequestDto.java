package com.example.ohmobackend.web.dto.memberDto;

import lombok.Getter;

public class MemberRequestDto {

    @Getter
    public static class SignupRequestDto {
        private String email;
        private String password;
        private String nickname;
    }

    @Getter
    public static class LoginRequestDto {
        private String email;
        private String password;
    }

    @Getter
    public static class RefreshTokenRequestDto {
        private String refreshToken;
    }

    @Getter
    public static class UpdateNicknameRequestDto {
        private String nickname;
    }
}
