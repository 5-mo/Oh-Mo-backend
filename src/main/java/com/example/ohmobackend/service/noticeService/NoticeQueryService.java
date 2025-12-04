package com.example.ohmobackend.service.noticeService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.noticeDto.NoticeResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface NoticeQueryService {

    public List<NoticeResponseDto.NoticeDto> getNotice(LocalDate date, Long groupId, Member member);
}
