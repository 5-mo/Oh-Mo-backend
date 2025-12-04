package com.example.ohmobackend.service.noticeService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.noticeDto.NoticeRequestDto;
import com.example.ohmobackend.web.dto.noticeDto.NoticeResponseDto;

public interface NoticeCommandService {

    public NoticeResponseDto.NoticeDto addNotice(NoticeRequestDto.AddNoticeDto requestDto, Member member);
}
