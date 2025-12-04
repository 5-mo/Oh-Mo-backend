package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.noticeService.NoticeCommandService;
import com.example.ohmobackend.web.dto.noticeDto.NoticeRequestDto;
import com.example.ohmobackend.web.dto.noticeDto.NoticeResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group")
public class NoticeController {

    private final NoticeCommandService noticeCommandService;

    @PostMapping("/notice")
    @Operation(summary = "그룹 공지사항 등록 API", description = "그룹 공지사항 등록 API 입니다.")
    public ApiResponse<NoticeResponseDto.NoticeDto> addNotice(
            @RequestBody NoticeRequestDto.AddNoticeDto request, @AuthUser Member member) {
        NoticeResponseDto.NoticeDto responseDto = noticeCommandService.addNotice(request, member);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_NOTICE_REGISTER_OK, responseDto);
    }

    @PatchMapping("/notice")
    @Operation(summary = "그룹 공지사항 수정 API", description = "그룹 공지사항 수정 API 입니다.")
    public ApiResponse<NoticeResponseDto.NoticeDto> patchNotice(
            @RequestParam(name = "noticeId") Long noticeId, @RequestBody NoticeRequestDto.PatchNoticeDto request, @AuthUser Member member) {
        NoticeResponseDto.NoticeDto responseDto = noticeCommandService.pathNotice(noticeId, request, member);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_NOTICE_MODIFY_OK, responseDto);
    }
}
