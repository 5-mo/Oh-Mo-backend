package com.example.ohmobackend.service.dayLogService;

import com.example.ohmobackend.converter.DayLogConverter;
import com.example.ohmobackend.domain.Daylog;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.repository.DayLogRepository;
import com.example.ohmobackend.web.dto.dayLogDto.DayLogResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DayLogQueryServiceImpl implements DayLogQueryService{

    private final DayLogRepository dayLogRepository;

    @Override
    public DayLogResponseDto.AddDayLogResponseDto getDayLogEmoji(Member member, LocalDate date) {
        Daylog byDateAndMember = dayLogRepository.findByDateAndMember(date, member);

        return DayLogConverter.toAddDayLogResponseDto(byDateAndMember);
    }

}
