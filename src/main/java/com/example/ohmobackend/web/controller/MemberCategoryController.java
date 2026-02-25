package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.memberCategoryService.MemberCategoryCommandService;
import com.example.ohmobackend.service.memberCategoryService.MemberCategoryQueryService;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryDtoRequest;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/category")
public class MemberCategoryController {

    final MemberCategoryCommandService memberCategoryCommandService;
    final MemberCategoryQueryService memberCategoryQueryService;

    @PostMapping()
    @Operation(summary = "카테고리 등록 API", description = "카테고리 등록 API 입니다.")
    public ApiResponse<MemberCategoryResponseDto.CategoryResponseDto> addMemberCategory(
            @RequestBody MemberCategoryDtoRequest.addCategoryRequest request, @AuthUser Member member) {
        MemberCategoryResponseDto.CategoryResponseDto responseDto = memberCategoryCommandService.addMemberCategory(request, member);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_CATEGORY_REGISTER_OK, responseDto);
    }

    @GetMapping()
    @Operation(summary = "카테고리 조회 API", description = "카테고리 조회 API 입니다.")
    public ApiResponse<List<MemberCategoryResponseDto.CategoryResponseDto>> getMemberCategory(
            @RequestParam(name = "schedule-type") ScheduleType scheduleType, @AuthUser Member member) {
        List<MemberCategoryResponseDto.CategoryResponseDto> response = memberCategoryQueryService.getMemberCategory(scheduleType, member);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_CATEGORY_OK, response);
    }

    @PatchMapping("/{categoryId}")
    @Operation(summary = "카테고리 수정 API", description = "카테고리 이름과 색상을 수정합니다.")
    public ApiResponse<MemberCategoryResponseDto.CategoryResponseDto> updateMemberCategory(
            @PathVariable(name = "categoryId") Long categoryId,
            @RequestBody MemberCategoryDtoRequest.UpdateCategoryRequest request,
            @AuthUser Member member) {
        MemberCategoryResponseDto.CategoryResponseDto response = memberCategoryCommandService.updateMemberCategory(categoryId, request, member);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_CATEGORY_UPDATE_OK, response);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "카테고리 삭제 API", description = "카테고리를 삭제합니다. 해당 카테고리의 일정도 함께 삭제됩니다.")
    public ApiResponse<Object> deleteMemberCategory(
            @PathVariable(name = "categoryId") Long categoryId,
            @AuthUser Member member) {
        memberCategoryCommandService.deleteMemberCategory(categoryId, member);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_CATEGORY_DELETE_OK, null);
    }

}
