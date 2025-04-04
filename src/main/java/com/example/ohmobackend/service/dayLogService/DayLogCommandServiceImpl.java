package com.example.ohmobackend.service.dayLogService;

import com.example.ohmobackend.converter.DayLogConverter;
import com.example.ohmobackend.domain.Daylog;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.repository.DayLogRepository;
import com.example.ohmobackend.web.dto.dayLogDto.DayLogRequestDto;
import com.example.ohmobackend.web.dto.dayLogDto.DayLogResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DayLogCommandServiceImpl implements DayLogCommandService{

    final DayLogRepository dayLogRepository;

    @Override
    public DayLogResponseDto.AddDayLogResponseDto addDayLog(Member member, DayLogRequestDto.AddDayLogRequestDto requestDto) {
        Daylog daylog = DayLogConverter.toEntity(requestDto, member);

        dayLogRepository.save(daylog);
        return DayLogConverter.toAddDayLogResponseDto(daylog);
    }
}
