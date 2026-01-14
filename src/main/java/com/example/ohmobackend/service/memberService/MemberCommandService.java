package com.example.ohmobackend.service.memberService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.memberDto.MemberRequestDto;
import com.example.ohmobackend.web.dto.memberDto.MemberResponseDto;

public interface MemberCommandService {

    public MemberResponseDto.SignupResponseDto signup(MemberRequestDto.SignupRequestDto request);
    public MemberResponseDto.LoginResponseDto login(MemberRequestDto.LoginRequestDto request);
    public Member getByEmail(String email);
    public MemberResponseDto.LoginResponseDto reissue(String refreshToken);

    public void logout(String token);
}
