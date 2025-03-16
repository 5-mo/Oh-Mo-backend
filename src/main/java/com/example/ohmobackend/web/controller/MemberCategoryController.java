package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.memberCategoryService.MemberCategoryCommandService;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryDtoRequest;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/category")
public class MemberCategoryController {

    final MemberCategoryCommandService memberCategoryCommandService;

    @PostMapping("/")
    @Operation(summary = "카테고리 등록 API", description = "카테고리 등록 API 입니다.")
    public ApiResponse<MemberCategoryResponseDto.addCategoryResponseDto> addMemberCategory(
            @RequestBody MemberCategoryDtoRequest.addCategoryRequest request, @AuthUser Member member) {
        MemberCategoryResponseDto.addCategoryResponseDto responseDto = memberCategoryCommandService.addMemberCategory(request, member);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_CATEGORY_OK, responseDto);
    }

}
