package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.service.UserService.UserCommandService;
import com.example.ohmobackend.web.dto.userDto.UserRequestDto;
import com.example.ohmobackend.web.dto.userDto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    final UserCommandService userCommandService;

    @PostMapping("/signup")
    public ApiResponse<UserResponseDto.SignupResponseDto> signup(@RequestBody UserRequestDto.SignupRequestDto request) {
        UserResponseDto.SignupResponseDto responseDto = userCommandService.signup(request);
        return ApiResponse.onSuccess(SuccessStatus.USER_SIGNUP_OK, responseDto);
    }

    @GetMapping("/test")
    public String gg() {
        return "안녕";
    }

}
