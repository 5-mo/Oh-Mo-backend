package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.JwtToken;
import com.example.ohmobackend.web.dto.memberDto.MemberRequestDto;
import com.example.ohmobackend.web.dto.memberDto.MemberResponseDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MemberConverter {


    public static Member toEntity(MemberRequestDto.SignupRequestDto dto, String password) {
        return Member.builder()
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .password(password)
                .build();
    }

    public static MemberResponseDto.SignupResponseDto toDto(Member member) {
        return MemberResponseDto.SignupResponseDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

    public static MemberResponseDto.LoginResponseDto toLoginResponseDto(Member member, JwtToken jwtToken) {
        return MemberResponseDto.LoginResponseDto.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .token(jwtToken)
                .build();
    }

}
