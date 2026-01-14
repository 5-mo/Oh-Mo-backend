package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.service.memberService.MemberCommandService;
import com.example.ohmobackend.web.dto.memberDto.MemberRequestDto;
import com.example.ohmobackend.web.dto.memberDto.MemberResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {

    final MemberCommandService memberCommandService;

    @PostMapping("/signup")
    @Operation(summary = "이메일 회원 가입 API",description = "이메일 회원 가입 API 입니다.")
    public ApiResponse<MemberResponseDto.SignupResponseDto> signup(@RequestBody MemberRequestDto.SignupRequestDto request) {
        MemberResponseDto.SignupResponseDto responseDto = memberCommandService.signup(request);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_SIGNUP_OK, responseDto);
    }

    @PostMapping("/login")
    @Operation(summary = "이메일 로그인 API",description = "이메일 로그인 API 입니다.")
    public ApiResponse<MemberResponseDto.LoginResponseDto> login(@RequestBody MemberRequestDto.LoginRequestDto request) {
        MemberResponseDto.LoginResponseDto responseDto = memberCommandService.login(request);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_LOGIN_OK, responseDto);
    }

    @GetMapping("/test")
    @Operation(summary = "테스트 API",description = "테스트")
    public String test() {
        return "테스트ㅡㅡ";
    }

    @PostMapping("/reissue")
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
}
