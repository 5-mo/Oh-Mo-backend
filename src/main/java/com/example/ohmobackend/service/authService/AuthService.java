package com.example.ohmobackend.service.authService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.memberDto.MemberRequestDto;
import com.example.ohmobackend.web.dto.memberDto.MemberResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {

    public MemberResponseDto.MemberInfoResponseDto signup(MemberRequestDto.SignupRequestDto request, MultipartFile profileImage);

    public MemberResponseDto.LoginResponseDto login(MemberRequestDto.LoginRequestDto request);

    public MemberResponseDto.LoginResponseDto reissue(String refreshToken);

    public void logout(String accessToken);

    public void withdraw(Member member);
}
