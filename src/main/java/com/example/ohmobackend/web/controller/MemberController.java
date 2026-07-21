package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.converter.MemberConverter;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.authService.AuthService;
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

    private final MemberCommandService memberCommandService;
    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "이메일 회원 가입 API", description = "이메일 회원 가입 API 입니다.")
    public ApiResponse<MemberResponseDto.MemberInfoResponseDto> signup(
            @RequestPart("request") MemberRequestDto.SignupRequestDto request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        MemberResponseDto.MemberInfoResponseDto responseDto =
                authService.signup(request, profileImage);

        return ApiResponse.onSuccess(SuccessStatus.MEMBER_SIGNUP_OK, responseDto);
    }

    @PostMapping("/login")
    @Operation(summary = "이메일 로그인 API", description = "이메일 로그인 API 입니다.")
    public ApiResponse<MemberResponseDto.LoginResponseDto> login(@RequestBody MemberRequestDto.LoginRequestDto request) {
        MemberResponseDto.LoginResponseDto responseDto = authService.login(request);
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

        MemberResponseDto.LoginResponseDto responseDto = authService.reissue(request.getRefreshToken());
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_REISSUE_OK, responseDto);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "로그아웃 시 Refresh Token 삭제")
    public ApiResponse<Void> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        authService.logout(accessToken);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_LOGOUT_OK, null);
    }

    @DeleteMapping("/withdraw")
    @Operation(summary = "회원 탈퇴 API", description = "회원 정보를 삭제하고 로그아웃 처리")
    public ApiResponse<Void> withdraw(@AuthUser Member member) {
        authService.withdraw(member);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_WITHDRAW_OK, null);
    }

    @GetMapping("")
    @Operation(summary = "회원 정보 조회 API", description = "회원 정보 조회")
    public ApiResponse<MemberResponseDto.MemberInfoResponseDto> getMember(@AuthUser Member member) {
        MemberResponseDto.MemberInfoResponseDto memberInfoResponseDto = MemberConverter.toMemberInfoResponseDto(member);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, memberInfoResponseDto);
    }

    @PatchMapping("nickname")
    @Operation(summary = "회원 닉네임 수정 API", description = "회원 닉네임 수정 ")
    public ApiResponse<MemberResponseDto.MemberInfoResponseDto> updateMemberNickname(@AuthUser Member member,
                                                                                       @RequestBody MemberRequestDto.UpdateNicknameRequestDto request) {
        MemberResponseDto.MemberInfoResponseDto memberInfoWithoutProfileDto = memberCommandService.updateMemberNickName(member, request);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, memberInfoWithoutProfileDto);
    }

    @PatchMapping(value= "profile-image", consumes = "multipart/form-data")
    @Operation(summary = "회원 프로필 사진 수정 API", description = "회원 프로필 사진 수정 ")
    public ApiResponse<MemberResponseDto.MemberInfoResponseDto>
    updateMemberProfileImage(@AuthUser Member member, @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        MemberResponseDto.MemberInfoResponseDto memberInfoWithoutProfileDto = memberCommandService.updateMemberProfileImage(member, profileImage);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, memberInfoWithoutProfileDto);
    }

    @PatchMapping("/password")
    @Operation(summary = "비밀번호 바꾸기")
    public ApiResponse<Object> updatePassword(@AuthUser Member member, @RequestBody MemberRequestDto.UpdatePasswordDto request) {
        memberCommandService.updatePassword(member, request);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_OK, null);
    }

    @PostMapping("/password/find")
    @Operation(summary = "비밀번호 찾기 - 인증 코드 발송 API", description = "이메일로 6자리 인증 코드를 발송합니다. 코드는 5분간 유효합니다.")
    public ApiResponse<Void> sendPasswordResetCode(@RequestBody MemberRequestDto.FindPasswordRequestDto request) {
        authService.sendPasswordResetCode(request);
        return ApiResponse.onSuccess(SuccessStatus.PASSWORD_FIND_CODE_SENT, null);
    }

    @PostMapping("/password/reset")
    @Operation(summary = "비밀번호 찾기 - 비밀번호 재설정 API", description = "인증 코드 검증 후 새 비밀번호로 변경합니다.")
    public ApiResponse<Void> resetPassword(@RequestBody MemberRequestDto.ResetPasswordRequestDto request) {
        authService.resetPassword(request);
        return ApiResponse.onSuccess(SuccessStatus.PASSWORD_RESET_OK, null);
    }

    @PatchMapping("/fcm-token")
    @Operation(summary = "FCM 토큰 등록 API", description = "푸시 알림을 위한 FCM 토큰을 등록합니다.")
    public ApiResponse<Void> updateFcmToken(@AuthUser Member member,
                                             @RequestBody MemberRequestDto.UpdateFcmTokenRequestDto request) {
        memberCommandService.updateFcmToken(member, request.getFcmToken());
        return ApiResponse.onSuccess(SuccessStatus.FCM_TOKEN_UPDATE_OK, null);
    }
}
