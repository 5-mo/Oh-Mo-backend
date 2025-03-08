package com.example.ohmobackend.service.UserService;

import com.example.ohmobackend.converter.UserConverter;
import com.example.ohmobackend.domain.User;
import com.example.ohmobackend.repository.UserRepository;
import com.example.ohmobackend.web.dto.userDto.UserRequestDto;
import com.example.ohmobackend.web.dto.userDto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService{

    private final UserRepository userRepository;

    @Override
    public UserResponseDto.SignupResponseDto signup(UserRequestDto.SignupRequestDto request) {
        User user = UserConverter.toEntity(request);

        User newUser = userRepository.save(user);

        return UserConverter.toDto(newUser);
    }
}
