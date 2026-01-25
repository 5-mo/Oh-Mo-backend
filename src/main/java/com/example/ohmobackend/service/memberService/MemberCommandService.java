package com.example.ohmobackend.service.memberService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.memberDto.MemberRequestDto;
import com.example.ohmobackend.web.dto.memberDto.MemberResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface MemberCommandService {

    public MemberResponseDto.MemberInfoResponseDto signup(MemberRequestDto.SignupRequestDto request, MultipartFile profileImage);
    public MemberResponseDto.LoginResponseDto login(MemberRequestDto.LoginRequestDto request);
    public Member getByEmail(String email);
    public MemberResponseDto.LoginResponseDto reissue(String refreshToken);

    public void logout(String token);

    public void withdraw(Member member);

    public MemberResponseDto.MemberInfoResponseDto updateMemberNickName(Member member, MemberRequestDto.UpdateNicknameRequestDto request);
    public MemberResponseDto.MemberInfoResponseDto updateMemberProfileImage(Member member, MultipartFile profileImage);
    public void updatePassword(Member member, MemberRequestDto.UpdatePasswordDto request);
}
