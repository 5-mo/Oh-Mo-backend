package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.Notice;
import com.example.ohmobackend.web.dto.noticeDto.NoticeRequestDto;
import com.example.ohmobackend.web.dto.noticeDto.NoticeResponseDto;

import java.time.LocalDate;
import java.util.List;

public class NoticeConverter {

    public static Notice toNoticeEntity(NoticeRequestDto.AddNoticeDto addNoticeDto, Group group) {
        return Notice.builder()
                .notice(addNoticeDto.getNotice())
                .date(addNoticeDto.getDate())
                .group(group)
                .build();
    }

    public static NoticeResponseDto.NoticeDto toNoticeDto(Notice notice) {
        return NoticeResponseDto.NoticeDto.builder()
                .notice(notice.getNotice())
                .date(notice.getDate())
                .groupId(notice.getGroup().getId())
                .build();
    }

    public static List<NoticeResponseDto.NoticeDto> toNoticeDtoList(List<Notice> notices) {
        return notices.stream()
                .map(NoticeConverter::toNoticeDto)
                .toList();
    }

    public static NoticeResponseDto.NoticeByMonthDto toNoticeByMonthDto(
            LocalDate date,
            List<Notice> notices
    ) {
        return NoticeResponseDto.NoticeByMonthDto.builder()
                .date(date)
                .notices(toNoticeDtoList(notices))
                .build();
    }

}
