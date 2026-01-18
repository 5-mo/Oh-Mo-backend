package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.converter.MemberConverter;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.memberService.MemberCommandService;
import com.example.ohmobackend.web.dto.memberDto.MemberRequestDto;
import com.example.ohmobackend.web.dto.memberDto.MemberResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {

    final MemberCommandService memberCommandService;

    @PostMapping(value = "/signup", consumes = "multipart/form-data")
    @Operation(summary = "이메일 회원 가입 API", description = "이메일 회원 가입 API 입니다.")
    public ApiResponse<MemberResponseDto.SignupResponseDto> signup(
            @RequestPart("request") MemberRequestDto.SignupRequestDto request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        MemberResponseDto.SignupResponseDto responseDto =
                memberCommandService.signup(request, profileImage);

        return ApiResponse.onSuccess(SuccessStatus.MEMBER_SIGNUP_OK, responseDto);
    }

    @PostMapping("/login")
    @Operation(summary = "이메일 로그인 API", description = "이메일 로그인 API 입니다.")
    public ApiResponse<MemberResponseDto.LoginResponseDto> login(@RequestBody MemberRequestDto.LoginRequestDto request) {
        MemberResponseDto.LoginResponseDto responseDto = memberCommandService.login(request);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_LOGIN_OK, responseDto);
    }

    @GetMapping("/test")
    @Operation(summary = "테스트 API", description = "테스트")
    public String test() {
        return "테스트ㅡㅡ" ;
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급 API")
    public ApiResponse<MemberResponseDto.LoginResponseDto> reissue(
            @RequestBody MemberRequestDto.RefreshTokenRequestDto request) {

        MemberResponseDto.LoginResponseDto responseDto = memberCommandService.reissue(request.getRefreshToken());
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_REISSUE_OK, responseDto);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "로그아웃 시 Refresh Token 삭제")
    public ApiResponse<Void> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        memberCommandService.logout(accessToken);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_LOGOUT_OK, null);
    }

    @DeleteMapping("/withdraw")
    @Operation(summary = "회원 탈퇴 API", description = "회원 정보를 삭제하고 로그아웃 처리")
    public ApiResponse<Void> withdraw(@AuthUser Member member) {
        memberCommandService.withdraw(member);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_WITHDRAW_OK, null);
    }

    @GetMapping("")
    @Operation(summary = "회원 정보 조회 API", description = "회원 정보 조회")
    public ApiResponse<MemberResponseDto.MemberInfoResponseDto> getMember(@AuthUser Member member) {
        MemberResponseDto.MemberInfoResponseDto memberInfoResponseDto = MemberConverter.toMemberInfoResponseDto(member);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, memberInfoResponseDto);
    }
}
