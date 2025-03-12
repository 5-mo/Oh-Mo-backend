package com.example.ohmobackend.service.memberService;

import com.example.ohmobackend.web.dto.memberDto.MemberRequestDto;
import com.example.ohmobackend.web.dto.memberDto.MemberResponseDto;

public interface MemberCommandService {

    public MemberResponseDto.SignupResponseDto signup(MemberRequestDto.SignupRequestDto request);
    public MemberResponseDto.LoginResponseDto login(MemberRequestDto.LoginRequestDto request);
}
