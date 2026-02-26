package com.example.ohmobackend.service.memberService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.memberDto.MemberRequestDto;
import com.example.ohmobackend.web.dto.memberDto.MemberResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface MemberCommandService {

    public MemberResponseDto.MemberInfoResponseDto updateMemberNickName(Member member, MemberRequestDto.UpdateNicknameRequestDto request);
    public MemberResponseDto.MemberInfoResponseDto updateMemberProfileImage(Member member, MultipartFile profileImage);
    public void updatePassword(Member member, MemberRequestDto.UpdatePasswordDto request);
    public void deleteMemberProfileImageFromS3(Member member);
    public String uploadProfileImageToS3(MultipartFile profileImage, String email);
}
