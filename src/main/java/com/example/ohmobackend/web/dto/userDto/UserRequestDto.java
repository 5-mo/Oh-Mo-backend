package com.example.ohmobackend.web.dto.userDto;

import lombok.Getter;

public class UserRequestDto {

    @Getter
    public static class SignupRequestDto {
        private String email;
        private String password;
        private String nickname;
    }
}
