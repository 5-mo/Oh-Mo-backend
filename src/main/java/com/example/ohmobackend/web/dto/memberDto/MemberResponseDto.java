package com.example.ohmobackend.web.dto.memberDto;

import com.example.ohmobackend.security.JwtToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupResponseDto {
        private String email;
        private String nickname;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponseDto {
        private Long memberId;
        private String email;
        private String nickname;
        private JwtToken token;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberInfoResponseDto {
        private Long memberId;
        private String email;
        private String nickname;
        private String profileImageUrl;
    }
}
