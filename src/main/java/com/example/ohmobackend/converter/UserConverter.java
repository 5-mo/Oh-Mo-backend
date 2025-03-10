package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.User;
import com.example.ohmobackend.web.dto.userDto.UserRequestDto;
import com.example.ohmobackend.web.dto.userDto.UserResponseDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserConverter {


    public static User toEntity(UserRequestDto.SignupRequestDto dto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        return User.builder()
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build();
    }

    public static UserResponseDto.SignupResponseDto toDto(User user) {
        return UserResponseDto.SignupResponseDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

}
