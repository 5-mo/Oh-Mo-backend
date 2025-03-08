package com.example.ohmobackend.service.UserService;

import com.example.ohmobackend.web.dto.userDto.UserRequestDto;
import com.example.ohmobackend.web.dto.userDto.UserResponseDto;

public interface UserCommandService {

    public UserResponseDto.SignupResponseDto signup(UserRequestDto.SignupRequestDto request);
}
