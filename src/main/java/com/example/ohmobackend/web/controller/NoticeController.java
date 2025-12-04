package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.noticeService.NoticeCommandService;
import com.example.ohmobackend.service.noticeService.NoticeQueryService;
import com.example.ohmobackend.web.dto.noticeDto.NoticeRequestDto;
import com.example.ohmobackend.web.dto.noticeDto.NoticeResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeCommandService noticeCommandService;
    private final NoticeQueryService noticeQueryService;

    @PostMapping
    @Operation(summary = "그룹 공지사항 등록 API", description = "그룹 공지사항 등록 API 입니다.")
    public ApiResponse<NoticeResponseDto.NoticeDto> addNotice(
            @RequestBody NoticeRequestDto.AddNoticeDto request, @AuthUser Member member) {
        NoticeResponseDto.NoticeDto responseDto = noticeCommandService.addNotice(request, member);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_NOTICE_REGISTER_OK, responseDto);
    }

    @PatchMapping
    @Operation(summary = "그룹 공지사항 수정 API", description = "그룹 공지사항 수정 API 입니다.")
    public ApiResponse<NoticeResponseDto.NoticeDto> patchNotice(
            @RequestParam(name = "noticeId") Long noticeId, @RequestBody NoticeRequestDto.PatchNoticeDto request, @AuthUser Member member) {
        NoticeResponseDto.NoticeDto responseDto = noticeCommandService.pathNotice(noticeId, request, member);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_NOTICE_MODIFY_OK, responseDto);
    }

    @DeleteMapping
    @Operation(summary = "그룹 공지사항 삭제 API", description = "그룹 공지사항 삭제 API 입니다.")
    public ApiResponse<Object> deleteNotice(
            @RequestParam(name = "noticeId") Long noticeId, @AuthUser Member member) {
        noticeCommandService.deleteNotice(noticeId, member);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_NOTICE_MODIFY_OK, null);
    }

    @GetMapping
    @Operation(summary = "그룹 공지사항 조회 API", description = "그룹 공지사항 조회 API 입니다.")
    public ApiResponse<List<NoticeResponseDto.NoticeDto>> getNotice(
            @RequestParam(name = "date")LocalDate date,
            @RequestParam(name = "groupId")Long groupId,
            @AuthUser Member member) {
        List<NoticeResponseDto.NoticeDto> notices = noticeQueryService.getNotice(date, groupId, member);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_NOTICE_OK, notices);
    }

    @GetMapping("/by-month")
    @Operation(summary = "월별 그룹 공지사항 조회 API")
    public ApiResponse<List<NoticeResponseDto.NoticeByMonthDto>> getNotice(
            @RequestParam(name = "year-month")String yearMonth,
            @RequestParam(name = "groupId")Long groupId,
            @AuthUser Member member) {
        List<NoticeResponseDto.NoticeByMonthDto> noticeByMonth = noticeQueryService.getNoticeByMonth(yearMonth, groupId, member);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_NOTICE_OK, noticeByMonth);
    }
}
