package com.example.ohmobackend.service.noticeService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.GroupHandler;
import com.example.ohmobackend.converter.NoticeConverter;
import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.Notice;
import com.example.ohmobackend.repository.GroupRepository;
import com.example.ohmobackend.repository.NoticeRepository;
import com.example.ohmobackend.web.dto.noticeDto.NoticeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeQueryServiceImpl implements NoticeQueryService {

    private final GroupRepository groupRepository;
    private final NoticeRepository noticeRepository;

    @Override
    public List<NoticeResponseDto.NoticeDto> getNotice(LocalDate date, Long groupId, Member member) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupHandler(ErrorStatus.GROUP_NOT_FOUND));

        List<Notice> noticesByDateAndGroup = noticeRepository.findAllByDateAndGroup(date, group);

        List<NoticeResponseDto.NoticeDto> notices = noticesByDateAndGroup.stream().map(
                notice -> NoticeConverter.toNoticeDto(notice)
        ).collect(Collectors.toList());
        return notices;
    }

    @Override
    public List<NoticeResponseDto.NoticeByMonthDto> getNoticeByMonth(String yearMonth, Long groupId, Member member) {
        LocalDate firstDayOfMonth = YearMonth.parse(yearMonth).atDay(1);
        LocalDate lastDayOfMonth = YearMonth.parse(yearMonth).atEndOfMonth();

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupHandler(ErrorStatus.GROUP_NOT_FOUND));

        List<Notice> notices = noticeRepository.findNoticesByGroupAndDate(group, firstDayOfMonth, lastDayOfMonth);

        Map<LocalDate, List<Notice>> noticeMap = notices.stream()
                .collect(Collectors.groupingBy(Notice::getDate));

        return noticeMap.entrySet().stream().map(
                entry -> NoticeConverter.toNoticeByMonthDto(entry.getKey(), entry.getValue())
        ).collect(Collectors.toList());
    }
}
