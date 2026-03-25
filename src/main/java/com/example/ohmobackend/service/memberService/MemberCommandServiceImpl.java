package com.example.ohmobackend.service.memberService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.AuthHandler;
import com.example.ohmobackend.apiPayload.exception.handler.MemberHandler;
import com.example.ohmobackend.converter.MemberCategoryConverter;
import com.example.ohmobackend.converter.MemberConverter;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberCategory;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.repository.MemberCategoryRepository;
import com.example.ohmobackend.repository.MemberRepository;
import com.example.ohmobackend.security.JwtToken;
import com.example.ohmobackend.security.principal.PrincipalDetailsService;
import com.example.ohmobackend.security.provider.TokenProvider;
import com.example.ohmobackend.service.fileService.FileUploadService;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryDtoRequest;
import com.example.ohmobackend.web.dto.memberDto.MemberRequestDto;
import com.example.ohmobackend.web.dto.memberDto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final MemberCategoryRepository memberCategoryRepository;
    private final FileUploadService fileUploadService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String DEFAULT_CATEGORY_COLOR_CODE = "#000000";
    private static final String DEFAULT_CATEGORY_NAME = "default";


    public void saveDefaultCategory(Member member, ScheduleType scheduleType) {
        MemberCategoryDtoRequest.addCategoryRequest todoCategory = MemberCategoryDtoRequest.addCategoryRequest.builder()
                .color(DEFAULT_CATEGORY_COLOR_CODE)
                .scheduleType(scheduleType)
                .categoryName(DEFAULT_CATEGORY_NAME)
                .build();
        MemberCategory defaultCategoryEntity = MemberCategoryConverter.toMemberCategoryEntity(todoCategory, member);
        memberCategoryRepository.save(defaultCategoryEntity);
    }

    @Override
    public MemberResponseDto.MemberInfoResponseDto updateMemberNickName(Member member, MemberRequestDto.UpdateNicknameRequestDto request) {
        member.updateNickname(request.getNickname());
        return MemberConverter.toMemberInfoResponseDto(member);
    }

    @Override
    public MemberResponseDto.MemberInfoResponseDto updateMemberProfileImage(Member member, MultipartFile profileImage) {
        String oldImageUrl = member.getProfileImageUrl();
        if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
            fileUploadService.delete(oldImageUrl);
        }

        String newImageUrl = uploadProfileImageToS3(profileImage, member.getEmail());

        member.updateProfileImage(newImageUrl);
        return MemberConverter.toMemberInfoResponseDto(member);
    }

    @Override
    public void updatePassword(Member member, MemberRequestDto.UpdatePasswordDto request) {
        if (!bCryptPasswordEncoder.matches(request.getOldPassword(), member.getPassword())) {
            throw new MemberHandler(ErrorStatus.INVALID_PASSWORD);
        }

        String encodedNewPassword = bCryptPasswordEncoder.encode(request.getNewPassword());
        member.updatePassword(encodedNewPassword);
    }

    public void deleteMemberProfileImageFromS3(Member member) {
        String imageUrl = member.getProfileImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            fileUploadService.delete(imageUrl);
        }
    }

    public String uploadProfileImageToS3(MultipartFile profileImage, String email) {
        if (profileImage == null || profileImage.isEmpty()) {
            return null;
        }
        String fileName = "profile/" + email + "_" + System.currentTimeMillis();
        return fileUploadService.upload(profileImage, fileName);
    }

    @Override
    public void updateFcmToken(Member member, String fcmToken) {
        member.updateFcmToken(fcmToken);
    }
}
