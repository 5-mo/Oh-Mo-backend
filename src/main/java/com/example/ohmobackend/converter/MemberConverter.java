package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.JwtToken;
import com.example.ohmobackend.web.dto.memberDto.MemberRequestDto;
import com.example.ohmobackend.web.dto.memberDto.MemberResponseDto;

public class MemberConverter {


    public static Member toEntity(MemberRequestDto.SignupRequestDto dto, String password, String profileImageUrl) {
        return Member.builder()
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .password(password)
                .profileImageUrl(profileImageUrl)
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
                .nickname(member.getNickname())
                .token(jwtToken)
                .build();
    }

    public static MemberResponseDto.MemberInfoResponseDto toMemberInfoResponseDto(Member member) {
        return MemberResponseDto.MemberInfoResponseDto.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }

}
