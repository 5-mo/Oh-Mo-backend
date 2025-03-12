package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.service.memberService.MemberCommandService;
import com.example.ohmobackend.web.dto.memberDto.MemberRequestDto;
import com.example.ohmobackend.web.dto.memberDto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {

    final MemberCommandService memberCommandService;

    @PostMapping("/signup")
    public ApiResponse<MemberResponseDto.SignupResponseDto> signup(@RequestBody MemberRequestDto.SignupRequestDto request) {
        MemberResponseDto.SignupResponseDto responseDto = memberCommandService.signup(request);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_SIGNUP_OK, responseDto);
    }

    @PostMapping("/login")
    public ApiResponse<MemberResponseDto.LoginResponseDto> login(@RequestBody MemberRequestDto.LoginRequestDto request) {
        MemberResponseDto.LoginResponseDto responseDto = memberCommandService.login(request);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_LOGIN_OK, responseDto);
    }

    @GetMapping("/test")
    public String test() {
        return "테스트";
    }

}
